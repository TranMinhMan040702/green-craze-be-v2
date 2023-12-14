package vn.com.greencraze.product.dto.request.variant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.product.enumeration.VariantStatus;

import java.math.BigDecimal;

public record UpdateVariantRequest(
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
        VariantStatus status
) {}
