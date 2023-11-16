package vn.com.greencraze.product.dto.request.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateBrandRequest(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        MultipartFile image,
        @NotBlank
        String code,
        @NotNull
        Boolean status
) {}
