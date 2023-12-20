package vn.com.greencraze.user.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.dto.request.cart.CreateCartItemRequest;
import vn.com.greencraze.user.dto.request.cart.UpdateCartItemRequest;
import vn.com.greencraze.user.dto.request.cart.UpdateUserCartRequest;
import vn.com.greencraze.user.dto.response.cart.CreateCartItemResponse;
import vn.com.greencraze.user.dto.response.cart.GetListCartItemResponse;

import java.util.List;

public interface ICartService {

    RestResponse<ListResponse<GetListCartItemResponse>> getCartByUser(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all);

    RestResponse<List<GetListCartItemResponse>> getCartItemByListId(List<Long> ids);

    RestResponse<CreateCartItemResponse> createCartItem(CreateCartItemRequest request);

    void updateCartItem(Long id, UpdateCartItemRequest request);

    void deleteOneCartItem(Long cartItemId);

    void deleteListCartItem(List<Long> ids);

    //call from another service
    void updateUserCart(UpdateUserCartRequest request);

}
