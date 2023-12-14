package vn.com.greencraze.address.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.greencraze.address.dto.response.province.GetListProvinceResponse;
import vn.com.greencraze.address.dto.response.province.GetOneProvinceResponse;
import vn.com.greencraze.address.entity.Province;
import vn.com.greencraze.commons.mapper.ReferenceMapper;

@Mapper(uses = {ReferenceMapper.class})
public interface ProvinceMapper {

    @Mapping(target = "id", ignore = true)
    Province idToProvince(String id);

    GetListProvinceResponse provinceToGetListProvinceResponse(Province province);

    GetOneProvinceResponse provinceToGetOneProvinceResponse(Province province);

}
