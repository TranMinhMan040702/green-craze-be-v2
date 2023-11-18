package vn.com.greencraze.product.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import vn.com.greencraze.product.enumeration.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
        @NotBlank
        String name,
        @NotBlank
        String shortDescription,
        @NotBlank
        String description,
        @NotBlank
        String code,
        @NotBlank
        String slug,
        @NotNull
        BigDecimal cost,
        @NotNull
        ProductStatus status,
        @NotNull
        Long categoryId,
        @NotNull
        Long brandId,
        @NotNull
        Long unitId,
        @NotEmpty
        List<MultipartFile> productImages,
        @NotEmpty
        List<String> variants
) {}
