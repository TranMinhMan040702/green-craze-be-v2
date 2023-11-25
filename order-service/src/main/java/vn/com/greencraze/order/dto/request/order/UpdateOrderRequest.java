package vn.com.greencraze.order.dto.request.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.order.enumeration.OrderStatus;

public record UpdateOrderRequest(
        @JsonIgnore
        Long id,
        @NotBlank
        OrderStatus status,
        @NotBlank
        String otherCancellation,
        @NotNull
        Long orderCancellationReasonId
) {
    public UpdateOrderRequest setId(Long id) {
        return new UpdateOrderRequest(id, status(), otherCancellation(), orderCancellationReasonId());
    }
}
