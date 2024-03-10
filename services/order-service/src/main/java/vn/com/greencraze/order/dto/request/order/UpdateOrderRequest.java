package vn.com.greencraze.order.dto.request.order;

import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.commons.enumeration.OrderStatus;

public record UpdateOrderRequest(
        @NotNull
        OrderStatus status,
        String otherCancellation,
        Long orderCancellationReasonId
) {
}
