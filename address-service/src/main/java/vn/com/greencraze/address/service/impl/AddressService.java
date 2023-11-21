package vn.com.greencraze.address.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.address.dto.request.address.CreateAddressRequest;
import vn.com.greencraze.address.dto.request.address.UpdateAddressRequest;
import vn.com.greencraze.address.dto.response.address.CreateAddressResponse;
import vn.com.greencraze.address.dto.response.address.GetListAddressResponse;
import vn.com.greencraze.address.dto.response.address.GetOneAddressResponse;
import vn.com.greencraze.address.dto.response.district.GetListDistrictResponse;
import vn.com.greencraze.address.dto.response.province.GetListProvinceResponse;
import vn.com.greencraze.address.dto.response.ward.GetListWardResponse;
import vn.com.greencraze.address.entity.Address;
import vn.com.greencraze.address.entity.District;
import vn.com.greencraze.address.entity.Province;
import vn.com.greencraze.address.entity.Ward;
import vn.com.greencraze.address.mapper.AddressMapper;
import vn.com.greencraze.address.mapper.DistrictMapper;
import vn.com.greencraze.address.mapper.ProvinceMapper;
import vn.com.greencraze.address.mapper.WardMapper;
import vn.com.greencraze.address.repository.AddressRepository;
import vn.com.greencraze.address.repository.DistrictRepository;
import vn.com.greencraze.address.repository.ProvinceRepository;
import vn.com.greencraze.address.repository.WardRepository;
import vn.com.greencraze.address.repository.specification.AddressSpecification;
import vn.com.greencraze.address.service.IAddressService;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.InvalidRequestException;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {
    private final AddressRepository addressRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    private final AddressMapper addressMapper;
    private final ProvinceMapper provinceMapper;
    private final DistrictMapper districtMapper;
    private final WardMapper wardMapper;
    private static final String RESOURCE_NAME = "Address";
    private static final List<String> SEARCH_FIELDS = List.of("receiver", "street", "email", "phone");

    @Override
    public RestResponse<ListResponse<GetListAddressResponse>> getListAddress(Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all) {
        AddressSpecification addressSpecification = new AddressSpecification();
        Specification<Address> sortable = addressSpecification.sortable(isSortAscending, columnName);
        Specification<Address> searchable = addressSpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListAddressResponse> responses = addressRepository
                .findAll(sortable.and(searchable), pageable)
                .map(addressMapper::addressToGetListAddressResponse);

        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneAddressResponse> getOneAddress(Long id, String userId) {
        return addressRepository.findByIdAndUserId(id, userId)
                .map(addressMapper::addressToGetOneAddressResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<GetOneAddressResponse> getDefaultAddress(String userId) {
        return addressRepository.findByUserIdAndIsDefault(userId, true)
                .map(addressMapper::addressToGetOneAddressResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "userId", userId));
    }

    @Override
    public RestResponse<List<GetListProvinceResponse>> getListProvince() {
        List<GetListProvinceResponse> provinces = provinceRepository.findAll(Pageable.unpaged())
                .map(provinceMapper::provinceToGetListProvinceResponse)
                .getContent();

        return RestResponse.ok(provinces);
    }

    @Override
    public RestResponse<List<GetListDistrictResponse>> getListDistrictByProvince(long provinceId) {
        Province province = provinceRepository.findById(provinceId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "provinceId", provinceId));

        return districtRepository.findAllByProvince(province)
                .map(districtMapper::listDistrictToGetListDistrictResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "provinceId", provinceId));
    }

    @Override
    public RestResponse<List<GetListWardResponse>> getListWardByDistrict(long districtId) {
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "districtId", districtId));

        return wardRepository.findAllByDistrict(district)
                .map(wardMapper::listWardToGetListWardResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "districtId", districtId));
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class, InvalidRequestException.class})
    @Override
    public RestResponse<CreateAddressResponse> createAddress(CreateAddressRequest request) {
        Province province = provinceRepository.findById(request.provinceId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "provinceId", request.provinceId()));
        District district = districtRepository.findById(request.districtId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "districtId", request.districtId()));
        Ward ward = wardRepository.findById(request.wardId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "wardId", request.wardId()));

        if (ward.getDistrict().getId() != district.getId() || district.getProvince().getId() != province.getId())
            throw new InvalidRequestException("Cannot identify combined address, may be unexpected provinceId, districtId, wardId");

        Address address = addressMapper.createAddressRequestToAddress(request);
        address.setIsDefault(true);

        addressRepository.findAll().forEach(x -> {
            x.setIsDefault(false);
            addressRepository.save(x);
        });

        addressRepository.save(address);

        return RestResponse.created(addressMapper.addressToCreateAddressResponse(address));
    }

    @Override
    public void updateAddress(UpdateAddressRequest request) {
        Province province = provinceRepository.findById(request.provinceId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "provinceId", request.provinceId()));
        District district = districtRepository.findById(request.districtId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "districtId", request.districtId()));
        Ward ward = wardRepository.findById(request.wardId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "wardId", request.wardId()));

        Address address = addressRepository.findByIdAndUserId(request.id(), request.userId())
                .map(a -> addressMapper.updateAddressFromUpdateAddressRequest(a, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", request.id()));

        if (ward.getDistrict().getId() != district.getId() || district.getProvince().getId() != province.getId())
            throw new InvalidRequestException("Cannot identify combined address, may be unexpected provinceId, districtId, wardId");

        addressRepository.save(address);
    }

    @Override
    public void deleteOneAddress(Long id, String userId) {
        Address address = addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        if (address.getStatus())
            throw new InvalidRequestException("Cannot handle to delete default address, please set another address to default and try again");

        address.setStatus(false);
        addressRepository.save(address);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class, InvalidRequestException.class})
    @Override
    public void setAddressDefault(Long id, String userId) {
        Address address = addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        address.setStatus(true);

        addressRepository.findAll().forEach(x -> {
            x.setIsDefault(false);
            addressRepository.save(x);
        });

        addressRepository.save(address);
    }
}
