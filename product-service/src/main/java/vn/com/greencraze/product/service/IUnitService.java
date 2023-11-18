package vn.com.greencraze.product.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.unit.CreateUnitRequest;
import vn.com.greencraze.product.dto.request.unit.UpdateUnitRequest;
import vn.com.greencraze.product.dto.response.unit.CreateUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetListUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetOneUnitResponse;

import java.util.List;

public interface IUnitService {

    RestResponse<ListResponse<GetListUnitResponse>> getListUnit(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    );

    RestResponse<GetOneUnitResponse> getOneUnit(Long id);

    RestResponse<CreateUnitResponse> createUnit(CreateUnitRequest request);

    void updateUnit(Long id, UpdateUnitRequest request);

    void deleteOneUnit(Long id);

    void deleteListUnit(List<Long> ids);

}
