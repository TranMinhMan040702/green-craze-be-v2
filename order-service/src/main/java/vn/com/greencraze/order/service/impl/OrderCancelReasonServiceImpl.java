package vn.com.greencraze.order.service.impl;

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
import vn.com.greencraze.order.dto.request.orderCancelReason.CreateOrderCancelReasonRequest;
import vn.com.greencraze.order.dto.request.orderCancelReason.UpdateOrderCancelReasonRequest;
import vn.com.greencraze.order.dto.response.orderCancelReason.CreateOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetListOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetOneOrderCancelReasonResponse;
import vn.com.greencraze.order.entity.OrderCancelReason;
import vn.com.greencraze.order.mapper.OrderCancelReasonMapper;
import vn.com.greencraze.order.repository.OrderCancelReasonRepository;
import vn.com.greencraze.order.repository.specification.OrderCancelReasonSpecification;
import vn.com.greencraze.order.service.IOrderCancelReasonService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCancelReasonServiceImpl implements IOrderCancelReasonService {
    private final OrderCancelReasonRepository orderCancelReasonRepository;
    private final OrderCancelReasonMapper orderCancelReasonMapper;

    private static final String RESOURCE_NAME = "OrderCancelReason";
    private static final List<String> SEARCH_FIELDS = List.of("name", "note");

    @Override
    public RestResponse<ListResponse<GetListOrderCancelReasonResponse>> getListOrderCancelReason(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    ) {
        OrderCancelReasonSpecification orderCancelReasonSpecification = new OrderCancelReasonSpecification();
        Specification<OrderCancelReason> sortable = orderCancelReasonSpecification.sortable(isSortAscending, columnName);
        Specification<OrderCancelReason> searchable = orderCancelReasonSpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListOrderCancelReasonResponse> responses = orderCancelReasonRepository
                .findAll(sortable.and(searchable), pageable)
                .map(orderCancelReasonMapper::orderCancelReasonToGetListOrderCancelReasonResponse);

        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneOrderCancelReasonResponse> getOneOrderCancelReason(Long id) {
        return orderCancelReasonRepository.findById(id)
                .map(orderCancelReasonMapper::orderCancelReasonToGetOneOrderCancelReasonResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<CreateOrderCancelReasonResponse> createOrderCancelReason(CreateOrderCancelReasonRequest request) {
        OrderCancelReason orderCancelReason = orderCancelReasonMapper.createOrderCancelReasonRequestToOrderCancelReason(request);
        orderCancelReasonRepository.save(orderCancelReason);

        return RestResponse.created(orderCancelReasonMapper.orderCancelReasonToCreateOrderCancelReasonResponse(orderCancelReason));
    }

    @Override
    public void updateOrderCancelReason(Long id, UpdateOrderCancelReasonRequest request) {
        OrderCancelReason orderCancelReason = orderCancelReasonRepository.findById(id)
                .map(b -> orderCancelReasonMapper.updateOrderCancelReasonFromUpdateOrderCancelReasonRequest(b, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        orderCancelReasonRepository.save(orderCancelReason);
    }

    @Override
    public void deleteOneOrderCancelReason(Long id) {
        OrderCancelReason orderCancelReason = orderCancelReasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        orderCancelReason.setStatus(false);
        orderCancelReasonRepository.save(orderCancelReason);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void deleteListOrderCancelReason(List<Long> ids) {
        for (Long id : ids) {
            OrderCancelReason orderCancelReason = orderCancelReasonRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            orderCancelReason.setStatus(false);
            orderCancelReasonRepository.save(orderCancelReason);
        }
    }
}
