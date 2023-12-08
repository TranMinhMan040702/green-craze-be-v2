package vn.com.greencraze.product.dto.request.variant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        @NotNull
        Long productId
) {}
