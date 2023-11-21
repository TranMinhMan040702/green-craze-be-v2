package vn.com.greencraze.address.dto.request.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAddressRequest(
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
        Long wardId
) {
    public CreateAddressRequest setUserId(String userId) {
        return new CreateAddressRequest(userId, receiver(), phone(), email(), street(), provinceId(), districtId(), wardId());
    }
}
