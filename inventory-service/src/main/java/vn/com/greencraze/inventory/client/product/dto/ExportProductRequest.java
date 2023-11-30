package vn.com.greencraze.inventory.client.product.dto;

import lombok.Builder;

@Builder
public record ExportProductRequest(
        Long id,
        Long quantity
) {}
