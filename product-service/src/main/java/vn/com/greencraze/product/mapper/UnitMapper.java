package vn.com.greencraze.product.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.product.dto.request.CreateUnitRequest;
import vn.com.greencraze.product.dto.response.CreateUnitResponse;
import vn.com.greencraze.product.dto.response.GetListUnitResponse;
import vn.com.greencraze.product.entity.Unit;

@Mapper
public interface UnitMapper {

    GetListUnitResponse unitToGetListUnitResponse(Unit unit);

    Unit createUnitRequestToUnit(CreateUnitRequest createUnitRequest);

    CreateUnitResponse unitToCreateUnitResponse(Unit unit);

}
