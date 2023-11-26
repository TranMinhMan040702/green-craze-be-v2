package vn.com.greencraze.user.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.dto.request.review.CreateReviewRequest;
import vn.com.greencraze.user.dto.request.review.ReplyReviewRequest;
import vn.com.greencraze.user.dto.request.review.UpdateReviewRequest;
import vn.com.greencraze.user.dto.response.review.CreateReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetListReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetOneReviewResponse;

import java.util.List;

public interface IReviewService {
    RestResponse<ListResponse<GetListReviewResponse>> getListReview(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all,
            Long productId, Long rating, Boolean status);

    RestResponse<List<Long>> getCountReview(Long productId);

    RestResponse<List<GetListReviewResponse>> getTop5ReviewLatest();

    RestResponse<GetOneReviewResponse> getOneReview(Long id);

    RestResponse<GetOneReviewResponse> getOneReviewByOrderItem(Long orderItemId);

    RestResponse<CreateReviewResponse> createReview(CreateReviewRequest request);

    void updateReview(Long id, UpdateReviewRequest request);

    void replyReview(Long id, ReplyReviewRequest request);

    void deleteOneReview(Long id);

    void toggleReview(Long id);

    void deleteListReview(List<Long> ids);
}
