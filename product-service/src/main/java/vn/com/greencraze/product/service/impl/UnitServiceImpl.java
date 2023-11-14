package vn.com.greencraze.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.CreateUnitRequest;
import vn.com.greencraze.product.dto.request.GetListUnitRequest;
import vn.com.greencraze.product.dto.response.CreateUnitResponse;
import vn.com.greencraze.product.dto.response.GetListUnitResponse;
import vn.com.greencraze.product.entity.Unit;
import vn.com.greencraze.product.mapper.UnitMapper;
import vn.com.greencraze.product.repository.UnitRepository;
import vn.com.greencraze.product.service.IUnitService;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements IUnitService {

    private final UnitRepository unitRepository;

    private final UnitMapper unitMapper;

    @Override
    public RestResponse<ListResponse<GetListUnitResponse>> getListUnit(GetListUnitRequest request) {
        // TODO: Review Specification for sort and filter and search
        Pageable pageable = request == null ? Pageable.unpaged()
                : PageRequest.of(request.pageIndex() - 1, request.pageSize());
        Page<GetListUnitResponse> responses =
                unitRepository.findAll(pageable).map(unitMapper::unitToGetListUnitResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<CreateUnitResponse> createUnit(CreateUnitRequest request) {
        Unit unit = unitMapper.createUnitRequestToUnit(request);
        unitRepository.save(unit);
        return RestResponse.created(unitMapper.unitToCreateUnitResponse(unit));
    }
}
