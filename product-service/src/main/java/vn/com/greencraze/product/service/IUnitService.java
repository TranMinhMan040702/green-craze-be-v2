package vn.com.greencraze.product.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.CreateUnitRequest;
import vn.com.greencraze.product.dto.request.GetListUnitRequest;
import vn.com.greencraze.product.dto.response.CreateUnitResponse;
import vn.com.greencraze.product.dto.response.GetListUnitResponse;

public interface IUnitService {
    RestResponse<ListResponse<GetListUnitResponse>> getListUnit(GetListUnitRequest request);

    RestResponse<CreateUnitResponse> createUnit(CreateUnitRequest request);
}
