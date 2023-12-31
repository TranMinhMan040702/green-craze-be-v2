package vn.com.greencraze.product.dto.request.category;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record CreateProductCategoryRequest(
        @NotBlank
        String name,
        @Nullable
        Long parentId,
        @Nullable
        MultipartFile image,
        @NotBlank
        String slug
) {}
