package vn.com.greencraze.order.dto.response.order;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.Instant;

public record GetTop5OrderLatestResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        BigDecimal totalAmount,
        Boolean paymentStatus,
        String status,
        String code,
        String deliveryMethod
) {}
