package vn.com.greencraze.user.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.user.dto.response.userFollowProduct.CreateUserFollowProductResponse;
import vn.com.greencraze.user.dto.response.userFollowProduct.GetListUserFollowProductResponse;
import vn.com.greencraze.user.entity.UserFollowProduct;

@Mapper(uses = {ReferenceMapper.class})
public interface UserFollowProductMapper {

    CreateUserFollowProductResponse userFollowProductToCreateUserFollowProductResponse(
            UserFollowProduct userFollowProduct);

    GetListUserFollowProductResponse userFollowProductToGetListFollowingProductResponse(
            UserFollowProduct userFollowProduct);

}
