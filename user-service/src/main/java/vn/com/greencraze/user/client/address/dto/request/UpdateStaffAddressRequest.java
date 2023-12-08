package vn.com.greencraze.user.client.address.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateStaffAddressRequest(
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
        Long wardId
) {
}
