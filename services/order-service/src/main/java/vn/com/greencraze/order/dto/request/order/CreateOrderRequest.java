package vn.com.greencraze.order.dto.request.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        String note,
        @NotNull
        Long paymentMethodId,
        @NotNull
        Long deliveryId,
        @NotEmpty
        @NotNull
        List<CreateOrderItemRequest> items
) {

}
