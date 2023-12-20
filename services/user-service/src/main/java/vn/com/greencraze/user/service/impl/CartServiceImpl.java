package vn.com.greencraze.user.service.impl;

import jakarta.transaction.Transactional;
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

        RestResponse<GetOneVariantResponse> variantResp = productServiceClient.getOneVariant(cartItem.getVariantId());
        if (variantResp == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", cartItem.getVariantId());
        }
        GetOneVariantResponse variant = variantResp.data();

        RestResponse<GetOneProductResponse> productResp = productServiceClient.getOneProduct(variant.productId());
        if (productResp == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", variant.productId());
        }
        GetOneProductResponse product = productResp.data();

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
                .map(this::mapToGetListCartItemResponse);

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
        if (request.quantity() <= 0) {
            throw new InvalidRequestException("Unexpected quantity, it must be a positive number");
        }

        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        RestResponse<GetOneVariantResponse> variantResp = productServiceClient.getOneVariant(request.variantId());
        if (variantResp == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", request.variantId());
        }
        GetOneVariantResponse variant = variantResp.data();

        RestResponse<GetOneProductResponse> productResp = productServiceClient.getOneProduct(variant.productId());
        if (productResp == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "productId", variant.productId());
        }
        GetOneProductResponse product = productResp.data();

        if (product.status() != GetOneProductResponse.ProductStatus.ACTIVE) {
            throw new InvalidRequestException("Unexpected variantId, product is not active");
        }

        long quantity = product.actualInventory();
        if (quantity < (request.quantity() * variant.quantity().longValue())) {
            throw new InvalidRequestException(
                    "Unexpected quantity, it must be less than or equal to product in inventory"
            );
        }

        CartItem cartItem = cartItemRepository.findByCartAndVariantId(cart, request.variantId());

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
            if ((long) cartItem.getQuantity() * variant.quantity() > product.actualInventory())
                throw new InvalidRequestException(
                        "Unexpected quantity, it must be less than or equal to product in inventory"
                );
        } else {
            cartItem = cartMapper.createCartItemRequestToCartItem(request);
            cartItem.setCart(cart);
        }

        cartItemRepository.save(cartItem);

        return RestResponse.ok(cartMapper.cartItemToCreateCartItemResponse(cartItem));
    }

    @Override
    public void updateCartItem(Long id, UpdateCartItemRequest request) {
        if (request.quantity() <= 0) {
            throw new InvalidRequestException("Unexpected quantity, it must be a positive number");
        }

        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        CartItem cartItem = cart.getCartItems()
                .stream().filter(x -> Objects.equals(x.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cartItem", id));

        RestResponse<GetOneVariantResponse> variantResp = productServiceClient.getOneVariant(cartItem.getVariantId());
        if (variantResp == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", cartItem.getVariantId());
        }
        GetOneVariantResponse variant = variantResp.data();

        RestResponse<GetOneProductResponse> productResp = productServiceClient.getOneProduct(variant.productId());
        if (productResp == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "productId", variant.productId());
        }
        GetOneProductResponse product = productResp.data();

        if (product.status() != GetOneProductResponse.ProductStatus.ACTIVE) {
            throw new InvalidRequestException("Unexpected variantId, product is not active");
        }

        cartItem.setQuantity(request.quantity());

        long quantity = product.actualInventory();
        if (quantity < (request.quantity() * variant.quantity().longValue())){
            cartItem.setQuantity((int) (quantity / variant.quantity()));
            if(cartItem.getQuantity() > 0){
                cartItemRepository.save(cartItem);
            }else{
                cartItemRepository.deleteById(id);
            }
            throw new InvalidRequestException(
                    "Unexpected quantity, it must be less than or equal to product in inventory"
            );
        }

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

        cartItemRepository.deleteById(cartItemId);
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
            cartItemRepository.deleteById(id);
        }

    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void updateUserCart(UpdateUserCartRequest request) {
        UserProfile user = userProfileRepository
                .findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", request.userId()));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", request.userId()));

        for (Long id : request.variantItemIds()) {
            cartItemRepository.deleteByCartAndVariantId(cart, id);
        }
    }

}
