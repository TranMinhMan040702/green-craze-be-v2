package vn.com.greencraze.product.dto.response.brand;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.time.Instant;

public record UpdateBrandResponse(
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
        String code,
        String description,
        String image,
        Boolean status
) {}
