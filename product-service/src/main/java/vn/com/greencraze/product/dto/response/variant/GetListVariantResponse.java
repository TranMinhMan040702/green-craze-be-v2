package vn.com.greencraze.product.dto.response.variant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.product.enumeration.VariantStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record GetListVariantResponse(
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
) {}
