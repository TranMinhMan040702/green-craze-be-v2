package vn.com.greencraze.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.auth.AuthFacade;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.user.client.product.ProductServiceClient;
import vn.com.greencraze.user.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.user.dto.request.userFollowProduct.FollowProductRequest;
import vn.com.greencraze.user.dto.response.userFollowProduct.CreateUserFollowProductResponse;
import vn.com.greencraze.user.dto.response.userFollowProduct.GetListUserFollowProductResponse;
import vn.com.greencraze.user.entity.UserFollowProduct;
import vn.com.greencraze.user.entity.UserProfile;
import vn.com.greencraze.user.mapper.UserFollowProductMapper;
import vn.com.greencraze.user.repository.UserFollowProductRepository;
import vn.com.greencraze.user.repository.UserProfileRepository;
import vn.com.greencraze.user.service.IUserFollowProductService;

@Service
@RequiredArgsConstructor
public class UserFollowProductServiceImpl implements IUserFollowProductService {

    private final UserFollowProductRepository userFollowProductRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserFollowProductMapper userFollowProductMapper;
    private final ProductServiceClient productServiceClient;
    private final AuthFacade authFacade;
    private static final String RESOURCE_NAME = "UserFollowProduct";

    @Override
    public RestResponse<CreateUserFollowProductResponse> followProduct(FollowProductRequest request) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        UserFollowProduct userFollowProduct = UserFollowProduct
                .builder()
                .user(user)
                .productId(request.productId())
                .build();

        userFollowProductRepository.save(userFollowProduct);

        return RestResponse.ok(userFollowProductMapper.userFollowProductToCreateUserFollowProductResponse(userFollowProduct));
    }

    @Override
    public void unfollowProduct(FollowProductRequest request) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        UserFollowProduct userFollowProduct = userFollowProductRepository
                .findByUserAndProductId(user, request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "product", request.productId()));

        userFollowProductRepository.delete(userFollowProduct);
    }

    @Override
    public RestResponse<ListResponse<GetListUserFollowProductResponse>> getListFollowingProduct(Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "user", userId));

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);

        Page<GetListUserFollowProductResponse> responses = userFollowProductRepository
                .findAllByUser(user, pageable)
                .map(userFollowProductMapper::userFollowProductToGetListFollowingProductResponse);

        for (GetListUserFollowProductResponse response : responses.getContent()) {
            RestResponse<GetOneProductResponse> productResponse = productServiceClient.getOneProduct(response.productId());
            GetListUserFollowProductResponse.ProductResponse product = userFollowProductMapper
                    .productResponseToGetListUserFollowProductResponse(productResponse.data());
            response = response.withProduct(product);
        }

        return RestResponse.ok(ListResponse.of(responses));
    }

}
