package vn.com.greencraze.order.client.address.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.time.Instant;

public record GetOneAddressResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String receiver,
        String phone,
        String email,
        String street,
        Boolean isDefault,
        Boolean status,
        ProvinceResponse province,
        DistrictResponse district,
        WardResponse ward
) {
    public record ProvinceResponse(
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
            String code
    ) {
    }

    public record DistrictResponse(
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
            ProvinceResponse province
    ) {
    }

    public record WardResponse(
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
            DistrictResponse district
    ) {
    }
}
