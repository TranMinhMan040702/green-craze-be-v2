package vn.com.greencraze.inventory.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateDocketWithTypeExportRequest(
        @NotNull
        Long productId,
        Long orderId,
        @NotNull
        Long quantity,
        String note
) {}
