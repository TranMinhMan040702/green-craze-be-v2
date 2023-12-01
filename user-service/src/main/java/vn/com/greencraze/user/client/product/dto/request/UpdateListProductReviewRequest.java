package vn.com.greencraze.user.client.product.dto.request;

import java.util.List;

public record UpdateListProductReviewRequest(
        List<UpdateOneProductReview> productReviews
) {
    public record UpdateOneProductReview(
            Long productId,
            Double rating
    ) {
    }
}
