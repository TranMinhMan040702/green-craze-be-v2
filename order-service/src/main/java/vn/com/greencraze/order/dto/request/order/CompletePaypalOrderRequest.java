package vn.com.greencraze.order.dto.request.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;

public record CompletePaypalOrderRequest(
        @JsonIgnore
        Long id,
        @NotBlank
        String paypalOrderId,
        @NotBlank
        String paypalOrderStatus
) {
    public CompletePaypalOrderRequest setId(Long id) {
        return new CompletePaypalOrderRequest(id, paypalOrderId(), paypalOrderStatus());
    }
}
