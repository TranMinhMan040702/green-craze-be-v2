package vn.com.greencraze.order.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.With;

@With
public record CreateOrderItemRequest(
        @NotNull
        Long variantId,
        @NotNull
        Integer quantity
) {}
