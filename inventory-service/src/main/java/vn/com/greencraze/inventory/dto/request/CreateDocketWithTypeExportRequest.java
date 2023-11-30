package vn.com.greencraze.inventory.dto.request;

public record CreateDocketWithTypeExportRequest(
        Long productId,
        Long orderId,
        Long quantity,
        String note
) {}
