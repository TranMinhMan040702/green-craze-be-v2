package vn.com.greencraze.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.auth.AuthFacade;
import vn.com.greencraze.commons.exception.InvalidRequestException;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.user.client.product.ProductServiceClient;
import vn.com.greencraze.user.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.user.client.product.dto.response.GetOneVariantResponse;
import vn.com.greencraze.user.dto.request.cart.CreateCartItemRequest;
import vn.com.greencraze.user.dto.request.cart.UpdateCartItemRequest;
import vn.com.greencraze.user.dto.request.cart.UpdateUserCartRequest;
import vn.com.greencraze.user.dto.response.cart.CreateCartItemResponse;
import vn.com.greencraze.user.dto.response.cart.GetListCartItemResponse;
import vn.com.greencraze.user.entity.Cart;
import vn.com.greencraze.user.entity.CartItem;
import vn.com.greencraze.user.entity.UserProfile;
import vn.com.greencraze.user.mapper.CartMapper;
import vn.com.greencraze.user.repository.CartItemRepository;
import vn.com.greencraze.user.repository.CartRepository;
import vn.com.greencraze.user.repository.UserProfileRepository;
import vn.com.greencraze.user.repository.specification.CartItemSpecification;
import vn.com.greencraze.user.service.ICartService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserProfileRepository userProfileRepository;
    private final CartMapper cartMapper;
    private final ProductServiceClient productServiceClient;
    private final AuthFacade authFacade;
    private static final String RESOURCE_NAME = "Cart";

    private GetListCartItemResponse mapToGetListCartItemResponse(CartItem cartItem) {

        GetOneVariantResponse variant = productServiceClient.getOneVariant(cartItem.getVariantId());
        if (variant == null)
            throw new ResourceNotFoundException("Variant", "id", cartItem.getVariantId());

        GetOneProductResponse product = productServiceClient.getOneProduct(variant.productId());
        if (product == null)
            throw new ResourceNotFoundException("Product", "id", variant.productId());


        GetListCartItemResponse res = cartMapper.cartItemToGetListCartItemResponse(cartItem)
                .withVariantName(variant.name())
                .withVariantQuantity(variant.quantity())
                .withSku(variant.sku())
                .withVariantPrice(variant.itemPrice())
                .withVariantPromotionalPrice(variant.promotionalItemPrice())
                .withTotalPrice(variant.totalPrice())
                .withTotalPromotionalPrice(variant.totalPromotionalPrice())
                .withProductSlug(product.slug())
                .withProductUnit(product.unit().name())
                .withProductName(product.name())
                .withProductImage(product.images().stream().findFirst().get().image())
                .withIsPromotion(variant.promotionalItemPrice() != null);

        return res;
    }

    @Override
    public RestResponse<ListResponse<GetListCartItemResponse>> getCartByUser(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        CartItemSpecification cartItemSpecification = new CartItemSpecification();
        Specification<CartItem> sortable = cartItemSpecification.sortable(isSortAscending, columnName);
        Specification<CartItem> filterable = cartItemSpecification.filterable(cart);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);

        Page<GetListCartItemResponse> responses = cartItemRepository
                .findAll(sortable.and(filterable), pageable)
                .map(cartMapper::cartItemToGetListCartItemResponse);

        responses.getContent().forEach(x -> {
                    GetListCartItemResponse item = x;
                    CartItem ci = cartItemRepository.findById(x.id())
                            .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cartItem", item.id()));
                    x = mapToGetListCartItemResponse(ci);
                }
        );

        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<List<GetListCartItemResponse>> getCartItemByListId(List<Long> ids) {
        List<GetListCartItemResponse> resp = new ArrayList<>();
        for (Long id : ids) {
            CartItem cartItem = cartItemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            resp.add(mapToGetListCartItemResponse(cartItem));
        }

        return RestResponse.ok(resp);
    }

    @Override
    public RestResponse<CreateCartItemResponse> createCartItem(CreateCartItemRequest request) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        CartItem cartItem = cartItemRepository.findByCartAndVariantId(cart, request.variantId());

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
        } else {
            cartItem = cartMapper.createCartItemRequestToCartItem(request);
            cartItem.setCart(cart);
        }

        cartItemRepository.save(cartItem);

        return RestResponse.ok(cartMapper.cartItemToCreateCartItemResponse(cartItem));
    }

    @Override
    public void updateCartItem(Long id, UpdateCartItemRequest request) {
        if (request.quantity() <= 0)
            throw new InvalidRequestException("Unexpected quantity, it must be a positive number");

        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        CartItem cartItem = cart.getCartItems()
                .stream().filter(x -> Objects.equals(x.getCart().getId(), cart.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cartItem", id));

        cartItem.setQuantity(request.quantity() + cartItem.getQuantity());

        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteOneCartItem(Long cartItemId) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        CartItem cartItem = cart.getCartItems()
                .stream().filter(x -> Objects.equals(x.getCart().getId(), cart.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cartItem", cartItemId));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void deleteListCartItem(List<Long> ids) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        for (Long id : ids) {
            CartItem cartItem = cart.getCartItems()
                    .stream().filter(x -> Objects.equals(x.getCart().getId(), cart.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cartItem", id));

            cartItemRepository.delete(cartItem);
        }

    }

    @Override
    public void updateUserCart(UpdateUserCartRequest request) {
        UserProfile user = userProfileRepository
                .findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", request.userId()));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", request.userId()));

        for (Long id : request.variantItemIds()) {
            CartItem cartItem = cart.getCartItems()
                    .stream().filter(x -> Objects.equals(x.getCart().getId(), cart.getId())
                            && Objects.equals(x.getVariantId(), id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cartItem", id));

            cartItemRepository.delete(cartItem);
        }
    }

}
