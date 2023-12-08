package vn.com.greencraze.product.dto.request.product;

import jakarta.validation.constraints.NotNull;

public record UpdateOneProductReviewRequest(
        @NotNull
        Double rating
) {
}
