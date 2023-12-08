package vn.com.greencraze.user.client.product.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateListProductReviewRequest(
        @NotNull
        @NotEmpty
        List<UpdateOneProductReview> productReviews
) {
    public record UpdateOneProductReview(
            @NotNull
            Long productId,
            @NotNull
            Double rating
    ) {
    }
}
