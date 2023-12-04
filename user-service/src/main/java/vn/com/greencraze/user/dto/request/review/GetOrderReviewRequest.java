package vn.com.greencraze.user.dto.request.review;

import java.util.List;

public record GetOrderReviewRequest(
        List<Long> orderItemIds
) {
}
