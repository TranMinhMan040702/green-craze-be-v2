package vn.com.greencraze.address.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.greencraze.address.dto.response.district.GetListDistrictResponse;
import vn.com.greencraze.address.dto.response.district.GetOneDistrictResponse;
import vn.com.greencraze.address.entity.District;
import vn.com.greencraze.commons.mapper.ReferenceMapper;

import java.util.List;

@Mapper(uses = {ReferenceMapper.class})
public interface DistrictMapper {

    @Mapping(target = "id", ignore = true)
    District idToDistrict(String id);

    GetListDistrictResponse districtToGetListDistrictResponse(District district);

    GetOneDistrictResponse districtToGetOneDistrictResponse(District district);

    List<GetListDistrictResponse> listDistrictToGetListDistrictResponse(List<District> districts);

}
