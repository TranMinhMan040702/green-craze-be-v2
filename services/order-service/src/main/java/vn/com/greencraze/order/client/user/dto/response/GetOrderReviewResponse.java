package vn.com.greencraze.order.client.user.dto.response;

import java.time.Instant;

public record GetOrderReviewResponse(
        Boolean isReview,
        Instant reviewedDate
) {
}
