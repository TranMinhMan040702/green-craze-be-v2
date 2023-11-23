package vn.com.greencraze.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.InvalidRequestException;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.user.dto.request.cart.CreateCartItemRequest;
import vn.com.greencraze.user.dto.request.cart.UpdateCartItemRequest;
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

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserProfileRepository userProfileRepository;
    private final CartMapper cartMapper;
    private static final String RESOURCE_NAME = "Cart";

    private GetListCartItemResponse mapToCartItemResponse(CartItem cartItem) {
//            GetListCartItemResponse res = new GetListCartItemResponse(
////                    cartItem.getId(),
////                    cartItem.getCreatedAt(),
////                    cartItem.getUpdatedAt(),
////                    cartItem.getCreatedBy(),
////                    cartItem.getUpdatedBy(),
////                    cartItem.getQuantity(),
////                    cartItem.getVariantId(),
////
////            );
        return null;
    }

    @Override
    public RestResponse<ListResponse<GetListCartItemResponse>> getCartByUser(Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all) {
        String userId = "";
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
                    x = mapToCartItemResponse(ci);
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
            resp.add(mapToCartItemResponse(cartItem));
        }

        return RestResponse.ok(resp);
    }

    @Override
    public RestResponse<CreateCartItemResponse> createCartItem(CreateCartItemRequest request) {
        String userId = "";
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
    public void updateCartItem(UpdateCartItemRequest request) {
        if (request.quantity() <= 0)
            throw new InvalidRequestException("Unexpected quantity, it must be a positive number");

        String userId = "";
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        CartItem cartItem = cart.getCartItems()
                .stream().filter(x -> x.getCart().getId() == cart.getId())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cartItem", request.cartItemId()));

        cartItem.setQuantity(request.quantity() + cartItem.getQuantity());

        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteOneCartItem(Long cartItemId) {
        String userId = "";
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        CartItem cartItem = cart.getCartItems()
                .stream().filter(x -> x.getCart().getId() == cart.getId())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cartItem", cartItemId));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void deleteListCartItem(List<Long> ids) {
        String userId = "";
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cart", userId));

        for (Long id : ids) {
            CartItem cartItem = cart.getCartItems()
                    .stream().filter(x -> x.getCart().getId() == cart.getId())
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cartItem", id));

            cartItemRepository.delete(cartItem);
        }

    }

}
