package vn.com.greencraze.order.dto.request.orderCancelReason;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderCancelReasonRequest(
        @NotBlank
        String name,

        @NotBlank
        String note,

        @NotNull
        Boolean status
) {
        
}
