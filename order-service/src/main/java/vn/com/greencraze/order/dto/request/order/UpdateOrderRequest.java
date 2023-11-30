package vn.com.greencraze.order.dto.request.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.order.enumeration.OrderStatus;

public record UpdateOrderRequest(
        @NotBlank
        OrderStatus status,
        @NotBlank
        String otherCancellation,
        @NotNull
        Long orderCancellationReasonId
) {
        
}
