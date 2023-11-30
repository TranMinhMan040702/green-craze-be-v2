package vn.com.greencraze.product.dto.request.product;

public record ImportProductRequest(
        Long id,
        Long quantity,
        Long actualInventory
) {}
