package vn.com.greencraze.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.product.dto.request.unit.CreateUnitRequest;
import vn.com.greencraze.product.dto.request.unit.UpdateUnitRequest;
import vn.com.greencraze.product.dto.response.unit.CreateUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetListUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetOneUnitResponse;
import vn.com.greencraze.product.entity.Unit;

@Mapper
public interface UnitMapper {

    GetListUnitResponse unitToGetListUnitResponse(Unit unit);

    GetOneUnitResponse unitToGetOneUnitResponse(Unit unit);

    Unit createUnitRequestToUnit(CreateUnitRequest createUnitRequest);

    CreateUnitResponse unitToCreateUnitResponse(Unit unit);

    Unit updateUnitFromUpdateUnitRequest(@MappingTarget Unit unit, UpdateUnitRequest updateUnitRequest);

}
