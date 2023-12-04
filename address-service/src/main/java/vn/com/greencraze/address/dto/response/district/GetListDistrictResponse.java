package vn.com.greencraze.address.dto.response.district;

import vn.com.greencraze.address.dto.response.province.GetOneProvinceResponse;

public record GetListDistrictResponse(
        Long id,
        String name,
        String code,
        GetOneProvinceResponse province
) {
}
