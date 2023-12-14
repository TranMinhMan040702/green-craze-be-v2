package vn.com.greencraze.inventory.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateDocketWithTypeImportRequest(
        @NotNull
        Long productId,
        @NotNull
        Long quantity,
        @NotNull
        Long actualInventory,
        String note
) {}
