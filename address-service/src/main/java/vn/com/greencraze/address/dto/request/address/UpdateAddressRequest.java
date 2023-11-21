package vn.com.greencraze.address.dto.request.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAddressRequest(
        @JsonIgnore
        Long id,
        @JsonIgnore
        String userId,
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
    public UpdateAddressRequest setIdAndUserId(Long id, String userId) {
        return new UpdateAddressRequest(id, userId, receiver(), phone(), email(), street(), provinceId(), districtId(), wardId(), isDefault());
    }
}
