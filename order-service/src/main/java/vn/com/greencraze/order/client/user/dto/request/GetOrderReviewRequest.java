package vn.com.greencraze.order.client.user.dto.request;

import java.util.List;

public record GetOrderReviewRequest(
        List<Long> orderItemIds
) {
}
