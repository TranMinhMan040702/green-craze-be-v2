package vn.com.greencraze.product.dto.request.sale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

public record CreateSaleRequest(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        MultipartFile image,
        @NotNull
        Instant startDate,
        @NotNull
        Instant endDate,
        @NotNull
        Double promotionalPercent,
        @NotBlank
        String slug,
        Boolean allProductCategory,
        List<Long> categoryIds
) {}
