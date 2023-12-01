package vn.com.greencraze.product.dto.request.product;

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
