package vn.com.greencraze.product.dto.request.product;

import jakarta.validation.constraints.NotNull;

public record ExportProductRequest(
        @NotNull
        Long id,
        @NotNull
        Long quantity
) {}
