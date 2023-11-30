package vn.com.greencraze.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.user.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.user.dto.request.review.CreateReviewRequest;
import vn.com.greencraze.user.dto.request.review.UpdateReviewRequest;
import vn.com.greencraze.user.dto.response.review.CreateReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetListReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetOneReviewResponse;
import vn.com.greencraze.user.entity.Review;

@Mapper(uses = {ReferenceMapper.class})
public interface ReviewMapper {

    GetListReviewResponse reviewToGetListReviewResponse(Review review);

    GetOneReviewResponse reviewToGetOneReviewResponse(Review review);

    CreateReviewResponse reviewToCreateReviewResponse(Review review);

    @Mapping(target = "image", ignore = true)
    Review createReviewRequestToReview(CreateReviewRequest createReviewRequest);

    @Mapping(target = "image", ignore = true)
    Review updateReviewRequestToReview(@MappingTarget Review review, UpdateReviewRequest updateReviewRequest);

    GetListReviewResponse.ProductResponse productResponseToGetListReviewProductResponse(GetOneProductResponse productResponse);

    GetOneReviewResponse.ProductResponse productResponseToGetOneReviewProductResponse(GetOneProductResponse productResponse);
}
