package vn.com.greencraze.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.product.dto.request.unit.CreateUnitRequest;
import vn.com.greencraze.product.dto.request.unit.UpdateUnitRequest;
import vn.com.greencraze.product.dto.response.unit.CreateUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetListUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetOneUnitResponse;
import vn.com.greencraze.product.entity.Unit;

@Mapper(uses = {ReferenceMapper.class})
public interface UnitMapper {

    @Mapping(target = "id", ignore = true)
    Unit idToUnit(String id);

    GetListUnitResponse unitToGetListUnitResponse(Unit unit);

    GetOneUnitResponse unitToGetOneUnitResponse(Unit unit);

    Unit createUnitRequestToUnit(CreateUnitRequest createUnitRequest);

    CreateUnitResponse unitToCreateUnitResponse(Unit unit);

    Unit updateUnitFromUpdateUnitRequest(@MappingTarget Unit unit, UpdateUnitRequest updateUnitRequest);

}
