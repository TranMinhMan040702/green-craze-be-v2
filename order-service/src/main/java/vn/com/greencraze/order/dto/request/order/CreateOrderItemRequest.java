package vn.com.greencraze.order.dto.request.order;

import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull
        Long variantId,
        @NotNull
        Integer quantity
) {
}
