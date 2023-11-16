package vn.com.greencraze.product.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.dto.request.unit.CreateUnitRequest;
import vn.com.greencraze.product.dto.request.unit.UpdateUnitRequest;
import vn.com.greencraze.product.dto.response.unit.CreateUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetListUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetOneUnitResponse;
import vn.com.greencraze.product.dto.response.unit.UpdateUnitResponse;
import vn.com.greencraze.product.entity.Unit;
import vn.com.greencraze.product.mapper.UnitMapper;
import vn.com.greencraze.product.repository.UnitRepository;
import vn.com.greencraze.product.repository.specification.BaseSpecification;
import vn.com.greencraze.product.service.IUnitService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements IUnitService {

    private final UnitRepository unitRepository;

    private final UnitMapper unitMapper;

    private static final String RESOURCE_NAME = "Unit";
    private static final List<String> SEARCH_FIELDS = List.of("name");

    @Override
    public RestResponse<ListResponse<GetListUnitResponse>> getListUnit(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    ) {
        BaseSpecification<Unit> unitSpecification = new BaseSpecification<>();
        Specification<Unit> sortable = unitSpecification.sortable(isSortAscending, columnName);
        Specification<Unit> searchable = unitSpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListUnitResponse> responses = unitRepository
                .findAll(sortable.and(searchable), pageable)
                .map(unitMapper::unitToGetListUnitResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneUnitResponse> getOneUnit(Long id) {
        return unitRepository.findById(id)
                .map(unitMapper::unitToGetOneUnitResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<CreateUnitResponse> createUnit(CreateUnitRequest request) {
        Unit unit = unitMapper.createUnitRequestToUnit(request);
        unitRepository.save(unit);
        return RestResponse.created(unitMapper.unitToCreateUnitResponse(unit));
    }

    @Override
    public RestResponse<UpdateUnitResponse> updateUnit(Long id, UpdateUnitRequest request) {
        return unitRepository
                .findById(id)
                .map(unit -> unitMapper.updateUnitFromUpdateUnitRequest(unit, request))
                .map(unitRepository::save)
                .map(unitMapper::unitToUpdateUnitResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public void deleteOneUnit(Long id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        unit.setStatus(false);
        unitRepository.save(unit);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void deleteListUnit(List<Long> ids) {
        for (Long id : ids) {
            Unit unit = unitRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            unit.setStatus(false);
            unitRepository.save(unit);
        }
    }

}
