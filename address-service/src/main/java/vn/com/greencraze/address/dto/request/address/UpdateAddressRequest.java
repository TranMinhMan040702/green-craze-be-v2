package vn.com.greencraze.address.dto.request.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAddressRequest(
        @NotBlank
        String receiver,
        @NotBlank
        String phone,
        @NotBlank
        String email,
        @NotBlank
        String street,
        @NotNull
        Long provinceId,
        @NotNull
        Long districtId,
        @NotNull
        Long wardId,
        Boolean isDefault
) {
}
