package vn.com.greencraze.inventory.dto.request;

public record CreateDocketWithTypeImportRequest(
        Long productId,
        Long quantity,
        Long actualInventory,
        String note
) {}
