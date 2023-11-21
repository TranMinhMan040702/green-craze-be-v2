package vn.com.greencraze.address.dto.response.address;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.address.dto.response.district.GetOneDistrictResponse;
import vn.com.greencraze.address.dto.response.province.GetOneProvinceResponse;
import vn.com.greencraze.address.dto.response.ward.GetOneWardResponse;

import java.time.Instant;

public record GetListAddressResponse(
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
        GetOneProvinceResponse province,
        GetOneDistrictResponse district,
        GetOneWardResponse ward
) {
}
