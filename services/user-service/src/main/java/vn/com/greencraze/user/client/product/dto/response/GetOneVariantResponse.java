package vn.com.greencraze.user.client.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record GetOneVariantResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @org.springframework.lang.Nullable
        @Schema(nullable = true)
        String createdBy,
        @org.springframework.lang.Nullable
        @Schema(nullable = true)
        String updatedBy,
        String name,
        String sku,
        Integer quantity,
        BigDecimal itemPrice,
        BigDecimal totalPrice,
        @Nullable
        BigDecimal totalPromotionalPrice,
        @NotNull
        BigDecimal promotionalItemPrice,
        VariantStatus status,
        Long productId
) {
    public enum VariantStatus {
        ACTIVE,
        INACTIVE
    }
}
