package vn.com.greencraze.product.dto.request.sale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import vn.com.greencraze.product.enumeration.SaleStatus;

import java.time.Instant;
import java.util.List;

public record UpdateSaleRequest(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        MultipartFile image,
        Instant startDate,
        Instant endDate,
        @NotNull
        Double promotionalPercent,
        @NotBlank
        String slug,
        @NotNull
        SaleStatus status,
        Boolean allProductCategory,
        List<Long> categoryIds
) {}
