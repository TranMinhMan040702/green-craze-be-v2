package vn.com.greencraze.user.dto.response.userFollowProduct;

import lombok.With;

import java.util.List;

@With
public record GetListUserFollowProductResponse(
        Long id,
        String name,
        String code,
        Double rating,
        String slug,
        ProductStatus status,
        ProductCategoryResponse productCategory,
        List<ProductImageResponse> images
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

    public record ProductImageResponse(
            Long id,
            Boolean isDefault,
            String image
    ) {
    }
}
