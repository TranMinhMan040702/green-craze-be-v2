package vn.com.greencraze.address.service;

import vn.com.greencraze.address.dto.request.address.CreateAddressRequest;
import vn.com.greencraze.address.dto.request.address.UpdateAddressRequest;
import vn.com.greencraze.address.dto.response.address.CreateAddressResponse;
import vn.com.greencraze.address.dto.response.address.GetListAddressResponse;
import vn.com.greencraze.address.dto.response.address.GetOneAddressResponse;
import vn.com.greencraze.address.dto.response.district.GetListDistrictResponse;
import vn.com.greencraze.address.dto.response.province.GetListProvinceResponse;
import vn.com.greencraze.address.dto.response.ward.GetListWardResponse;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;

import java.util.List;

public interface IAddressService {
    RestResponse<ListResponse<GetListAddressResponse>> getListAddress(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    );

    RestResponse<GetOneAddressResponse> getOneAddress(Long id, String userId);

    RestResponse<GetOneAddressResponse> getDefaultAddress(String userId);

    RestResponse<List<GetListProvinceResponse>> getListProvince();

    RestResponse<List<GetListDistrictResponse>> getListDistrictByProvince(long provinceId);

    RestResponse<List<GetListWardResponse>> getListWardByDistrict(long districtId);

    RestResponse<CreateAddressResponse> createAddress(CreateAddressRequest request);

    void updateAddress(UpdateAddressRequest request);

    void deleteOneAddress(Long id, String userId);

    void setAddressDefault(Long id, String userId);


}
