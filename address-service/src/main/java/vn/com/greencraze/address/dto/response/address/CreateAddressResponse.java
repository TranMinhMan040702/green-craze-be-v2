package vn.com.greencraze.address.dto.response.address;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.time.Instant;

public record CreateAddressResponse(
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
        Long provinceId,
        Long districtId,
        Long wardId
) {
}
