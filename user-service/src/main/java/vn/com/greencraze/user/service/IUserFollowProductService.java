package vn.com.greencraze.user.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.dto.request.userFollowProduct.FollowProductRequest;
import vn.com.greencraze.user.dto.response.userFollowProduct.CreateUserFollowProductResponse;
import vn.com.greencraze.user.dto.response.userFollowProduct.GetListUserFollowProductResponse;

public interface IUserFollowProductService {

    RestResponse<CreateUserFollowProductResponse> followProduct(FollowProductRequest request);

    void unfollowProduct(FollowProductRequest request);

    RestResponse<ListResponse<GetListUserFollowProductResponse>> getListFollowingProduct(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    );

}
