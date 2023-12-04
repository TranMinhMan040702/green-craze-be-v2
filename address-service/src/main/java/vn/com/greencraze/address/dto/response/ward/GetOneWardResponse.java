package vn.com.greencraze.address.dto.response.ward;

import vn.com.greencraze.address.dto.response.district.GetOneDistrictResponse;

public record GetOneWardResponse(
        Long id,
        String name,
        String code,
        GetOneDistrictResponse district
) {
}
