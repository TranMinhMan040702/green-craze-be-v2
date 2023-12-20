package vn.com.greencraze.user.dto.request.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.user.enumeration.GenderType;
import vn.com.greencraze.user.enumeration.IdentityStatus;
import vn.com.greencraze.user.enumeration.StaffType;

import java.time.Instant;

public record CreateStaffRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String phone,
        @NotNull
        Instant dob,
        @NotNull
        GenderType gender,
        @NotNull
        StaffType type,
        @NotNull
        IdentityStatus status,
        @Valid
        AddressRequest address
) {

    public record AddressRequest(
            @NotBlank
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
    ) {}

}
