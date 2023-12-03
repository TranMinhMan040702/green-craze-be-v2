package vn.com.greencraze.user.dto.response.review;

import lombok.With;

import java.time.Instant;

@With
public record GetOrderReviewResponse(
        Boolean isReview,
        Instant reviewedDate
) {
}
