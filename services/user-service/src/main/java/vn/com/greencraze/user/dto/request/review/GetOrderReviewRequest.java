package vn.com.greencraze.user.dto.request.review;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GetOrderReviewRequest(
        @NotNull
        @NotEmpty
        List<Long> orderItemIds
) {
}
