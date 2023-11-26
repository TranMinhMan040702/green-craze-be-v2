package vn.com.greencraze.user.dto.request.user;

import vn.com.greencraze.user.enumeration.StaffType;

public record UpdateStaffRequest(
        StaffType type,
        String password
) {

    public record AddressRequest(
            String userId,
            Long id,
            String receiver,
            String phone,
            String email,
            String street,
            Long provinceId,
            Long districtId,
            Long wardId
    ) {}

}
