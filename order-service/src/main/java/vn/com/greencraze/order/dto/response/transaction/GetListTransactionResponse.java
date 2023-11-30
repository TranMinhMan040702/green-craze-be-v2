package vn.com.greencraze.order.dto.response.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.Instant;

public record GetListTransactionResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String paymentMethod,
        @Nullable
        Instant paidAt,
        @Nullable
        Instant completeAt,
        BigDecimal totalPay,
        String paypalOrderId,
        String paypalOrderStatus,
        String orderCode
) {
        
}
