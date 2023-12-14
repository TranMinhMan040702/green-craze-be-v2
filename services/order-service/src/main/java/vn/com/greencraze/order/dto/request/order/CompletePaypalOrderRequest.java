package vn.com.greencraze.order.dto.request.order;

import jakarta.validation.constraints.NotBlank;

public record CompletePaypalOrderRequest(
        @NotBlank
        String paypalOrderId,
        @NotBlank
        String paypalOrderStatus
) {

}
