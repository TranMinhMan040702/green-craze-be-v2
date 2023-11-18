package vn.com.greencraze.product.dto.request.product;

import vn.com.greencraze.product.enumeration.ProductStatus;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        String shortDescription,
        String description,
        String code,
        String slug,
        BigDecimal cost,
        ProductStatus status,
        Long categoryId,
        Long brandId,
        Long unitId
) {}
