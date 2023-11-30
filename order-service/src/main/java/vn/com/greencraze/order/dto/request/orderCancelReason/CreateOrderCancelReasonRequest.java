package vn.com.greencraze.order.dto.request.orderCancelReason;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderCancelReasonRequest(
        @NotBlank
        String name,

        @NotBlank
        String note
) {}
