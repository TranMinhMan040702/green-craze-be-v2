package vn.com.greencraze.product.dto.request.brand;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UpdateBrandRequest(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @Nullable
        MultipartFile image,
        @NotBlank
        String code,
        @NotNull
        Boolean status
) {}
