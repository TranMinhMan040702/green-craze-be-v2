package vn.com.greencraze.user.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.user.dto.request.cart.CreateCartItemRequest;
import vn.com.greencraze.user.dto.response.cart.CreateCartItemResponse;
import vn.com.greencraze.user.dto.response.cart.GetListCartItemResponse;
import vn.com.greencraze.user.entity.CartItem;

@Mapper(uses = {ReferenceMapper.class})
public interface CartMapper {

    GetListCartItemResponse cartItemToGetListCartItemResponse(CartItem cartItem);

    CartItem createCartItemRequestToCartItem(CreateCartItemRequest createCartItemRequest);

    CreateCartItemResponse cartItemToCreateCartItemResponse(CartItem cartItem);

}
