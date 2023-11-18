package vn.com.greencraze.product.dto.request.image;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateProductImageRequest(
        @NotNull
        MultipartFile image,
        @NotNull
        Long productId
) {}
