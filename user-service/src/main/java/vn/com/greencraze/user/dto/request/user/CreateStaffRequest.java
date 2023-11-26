package vn.com.greencraze.user.dto.request.user;

import vn.com.greencraze.user.enumeration.GenderType;
import vn.com.greencraze.user.enumeration.IdentityStatus;
import vn.com.greencraze.user.enumeration.StaffType;

import java.time.Instant;

public record CreateStaffRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String phone,
        Instant dob,
        GenderType genderType,
        StaffType type,
        IdentityStatus status,
        AddressRequest address
) {

    public record AddressRequest(
            String userId,
            String receiver,
            String phone,
            String email,
            String street,
            Long provinceId,
            Long districtId,
            Long wardId
    ) {}

}
