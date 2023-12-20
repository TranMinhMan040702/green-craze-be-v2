package vn.com.greencraze.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.inventory.enumeration.DocketType;

import java.time.Instant;

public record GetListDocketByProductResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        Long orderId,
        Long quantity,
        DocketType type,
        String code,
        String note
) {}
