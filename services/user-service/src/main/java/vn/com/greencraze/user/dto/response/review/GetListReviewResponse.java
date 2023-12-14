package vn.com.greencraze.user.dto.response.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;
import org.springframework.lang.Nullable;
import vn.com.greencraze.user.dto.response.user.GetOneUserResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@With
public record GetListReviewResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String userId,
        Long productId,
        Long orderItemId,
        String title,
        String content,
        Integer rating,
        String image,
        String reply,
        Boolean status,
        String variantName,
        ProductResponse product,
        GetOneUserResponse user
) {

    public record ProductResponse(
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
        public enum ProductStatus {
            ACTIVE,
            INACTIVE,
            SOLD_OUT
        }

        public record ProductCategoryResponse(
                Long id,
                String name,
                String image,
                String slug
        ) {
        }

        public record BrandResponse(
                Long id,
                String name,
                String code,
                String description,
                String image
        ) {
        }

        public record UnitResponse(
                Long id,
                String name
        ) {
        }

        public record ProductImageResponse(
                Long id,
                String image
        ) {
        }

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
        ) {
            public enum VariantStatus {
                ACTIVE,
                INACTIVE
            }
        }

    }

}
