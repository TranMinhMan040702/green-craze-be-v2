package vn.com.greencraze.address.dto.response.address;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.time.Instant;

public record GetListAddressByUserIdResponse(
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
            String name,
            String code
    ) {}

    public record DistrictResponse(
            Long id,
            String name,
            String code,
            ProvinceResponse province
    ) {}

    public record WardResponse(
            Long id,
            String name,
            String code,
            DistrictResponse district
    ) {}

}
