package vn.com.greencraze.commons.domain.aggreate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import vn.com.greencraze.commons.enumeration.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CreateOrderAggregate(
        Long id,
        String userId,
        BigDecimal totalAmount,
        OrderStatus status,
        @NotEmpty
        @NotNull
        List<OrderItemAggregate> items
) {

    @Builder
    public record OrderItemAggregate(
            Long id,
            @NotNull
            Long variantId,
            @NotNull
            Integer quantity
    ) {}

}
