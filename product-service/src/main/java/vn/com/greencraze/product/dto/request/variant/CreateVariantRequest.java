package vn.com.greencraze.product.dto.request.variant;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.product.enumeration.VariantStatus;

import java.math.BigDecimal;

public record CreateVariantRequest(
        @NotBlank
        String name,
        @NotBlank
        String sku,
        @NotNull
        Long quantity,
        @NotNull
        BigDecimal itemPrice,
        @NotNull
        BigDecimal totalPrice,
        @Nullable
        BigDecimal promotionalItemPrice,
        @Nullable
        BigDecimal totalPromotionalPrice,
        @NotNull
        VariantStatus status,
        @NotNull
        Long productId
) {}
