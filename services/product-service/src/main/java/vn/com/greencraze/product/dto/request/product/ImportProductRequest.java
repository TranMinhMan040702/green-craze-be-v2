package vn.com.greencraze.product.dto.request.product;

import jakarta.validation.constraints.NotNull;

public record ImportProductRequest(
        @NotNull
        Long id,
        @NotNull
        Long quantity,
        @NotNull
        Long actualInventory
) {}
