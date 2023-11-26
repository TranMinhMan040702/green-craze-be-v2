package vn.com.greencraze.address.dto.response.district;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.address.dto.response.province.GetOneProvinceResponse;

import java.time.Instant;

public record GetListDistrictResponse(
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
        GetOneProvinceResponse province
) {
}
