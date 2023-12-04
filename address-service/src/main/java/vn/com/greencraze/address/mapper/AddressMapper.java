package vn.com.greencraze.address.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.address.dto.request.address.CreateAddressRequest;
import vn.com.greencraze.address.dto.request.address.UpdateAddressRequest;
import vn.com.greencraze.address.dto.response.address.CreateAddressResponse;
import vn.com.greencraze.address.dto.response.address.GetListAddressResponse;
import vn.com.greencraze.address.dto.response.address.GetOneAddressResponse;
import vn.com.greencraze.address.entity.Address;
import vn.com.greencraze.commons.mapper.ReferenceMapper;

@Mapper(uses = {ReferenceMapper.class, ProvinceMapper.class, DistrictMapper.class, WardMapper.class})
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    Address idToAddress(String id);

    GetListAddressResponse addressToGetListAddressResponse(Address address);

    GetOneAddressResponse addressToGetOneAddressResponse(Address address);

    @Mapping(source = "provinceId", target = "province")
    @Mapping(source = "districtId", target = "district")
    @Mapping(source = "wardId", target = "ward")
    Address createAddressRequestToAddress(CreateAddressRequest createAddressRequest);

    CreateAddressResponse addressToCreateAddressResponse(Address address);

    @Mapping(source = "provinceId", target = "province")
    @Mapping(source = "districtId", target = "district")
    @Mapping(source = "wardId", target = "ward")
    Address updateAddressFromUpdateAddressRequest(
            @MappingTarget Address address, UpdateAddressRequest updateAddressRequest);

}
