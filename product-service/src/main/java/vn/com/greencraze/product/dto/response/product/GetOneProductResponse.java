package vn.com.greencraze.product.dto.response.product;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.product.enumeration.ProductStatus;
import vn.com.greencraze.product.enumeration.VariantStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record GetOneProductResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String name,
        String shortDescription,
        String description,
        String code,
        Long quantity,
        Long actualInventory,
        Long sold,
        Double rating,
        String slug,
        BigDecimal cost,
        ProductStatus status,
        ProductCategoryResponse productCategory,
        BrandResponse brand,
        UnitResponse unit,
        List<ProductImageResponse> images,
        List<VariantResponse> variants
) {

    public record ProductCategoryResponse(
            Long id,
            String name,
            String image,
            String slug
    ) {}

    public record BrandResponse(
            Long id,
            String name,
            String code,
            String description,
            String image
    ) {}

    public record UnitResponse(
            Long id,
            String name
    ) {}

    public record ProductImageResponse(
            Long id,
            String image
    ) {}

    public record VariantResponse(
            Long id,
            String name,
            String sku,
            Integer quantity,
            BigDecimal itemPrice,
            BigDecimal totalPrice,
            BigDecimal totalPromotionalPrice,
            BigDecimal promotionalItemPrice,
            VariantStatus status
    ) {}

}
