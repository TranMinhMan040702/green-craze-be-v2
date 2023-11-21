package vn.com.greencraze.address.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.greencraze.address.dto.response.ward.GetListWardResponse;
import vn.com.greencraze.address.dto.response.ward.GetOneWardResponse;
import vn.com.greencraze.address.entity.Ward;
import vn.com.greencraze.commons.mapper.ReferenceMapper;

import java.util.List;

@Mapper(uses = {ReferenceMapper.class})
public interface WardMapper {
    @Mapping(target = "id", ignore = true)
    Ward idToWard(String id);

    GetListWardResponse wardToGetListWardResponse(Ward ward);

    GetOneWardResponse wardToGetOneWardResponse(Ward ward);

    List<GetListWardResponse> listWardToGetListWardResponse(List<Ward> wards);
}
