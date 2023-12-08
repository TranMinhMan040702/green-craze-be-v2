package vn.com.greencraze.user.client.product.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateOneProductReviewRequest(
        @NotNull
        Double rating
) {
}
