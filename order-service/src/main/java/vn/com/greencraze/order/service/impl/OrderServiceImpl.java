package vn.com.greencraze.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.order.dto.request.order.CompletePaypalOrderRequest;
import vn.com.greencraze.order.dto.request.order.CreateOrderRequest;
import vn.com.greencraze.order.dto.request.order.UpdateOrderRequest;
import vn.com.greencraze.order.dto.response.order.CreateOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderItemResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderResponse;
import vn.com.greencraze.order.entity.Order;
import vn.com.greencraze.order.entity.OrderItem;
import vn.com.greencraze.order.mapper.OrderItemMapper;
import vn.com.greencraze.order.mapper.OrderMapper;
import vn.com.greencraze.order.repository.OrderItemRepository;
import vn.com.greencraze.order.repository.OrderRepository;
import vn.com.greencraze.order.repository.specification.OrderSpecification;
import vn.com.greencraze.order.service.IOrderService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private static final String RESOURCE_NAME = "Order";
    private static final List<String> SEARCH_FIELDS = List.of("code");

    @Override
    public RestResponse<ListResponse<GetListOrderResponse>> getListOrder(Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all, String status) {
        OrderSpecification orderSpecification = new OrderSpecification();
        Specification<Order> sortable = orderSpecification.sortable(isSortAscending, columnName);
        Specification<Order> searchable = orderSpecification.searchable(SEARCH_FIELDS, search);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, size);

        Page<GetListOrderResponse> orders = orderRepository
                .findAll(sortable.and(searchable), pageable)
                .map(orderMapper::orderToGetListOrderResponse);

        return RestResponse.ok(ListResponse.of(orders));
    }

    @Override
    public RestResponse<ListResponse<GetListOrderResponse>> getListUserOrder(Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all, String status) {
        String userId = "";

        OrderSpecification orderSpecification = new OrderSpecification();
        Specification<Order> sortable = orderSpecification.sortable(isSortAscending, columnName);
        Specification<Order> searchable = orderSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Order> filterable = orderSpecification.filterable(userId, status);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, size);

        Page<GetListOrderResponse> orders = orderRepository
                .findAll(sortable.and(searchable).and(filterable), pageable)
                .map(orderMapper::orderToGetListOrderResponse);

        return RestResponse.ok(ListResponse.of(orders));
    }

    @Override
    public RestResponse<List<GetListOrderResponse>> getTop5OrderLastest() {
        OrderSpecification orderSpecification = new OrderSpecification();
        Specification<Order> sortable = orderSpecification.sortable(false, "createdAt");
        Pageable pageable = PageRequest.of(1, 5);

        List<GetListOrderResponse> orders = orderRepository
                .findAll(sortable, pageable)
                .map(orderMapper::orderToGetListOrderResponse).getContent();

        return RestResponse.ok(orders);
    }

    @Override
    public RestResponse<GetOneOrderResponse> getOneOrder(Long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Order", id));

        List<GetListOrderItemResponse> items = getOrderItemResponse(order.getOrderItems());

        GetOneOrderResponse orderResponse = orderMapper.orderToGetOneOrderResponse(order);

        return RestResponse.ok(orderResponse);
    }

    @Override
    public RestResponse<GetOneOrderResponse> getOneOrderByCode(String code) {
        Order order = orderRepository
                .findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Order", code));

        List<GetListOrderItemResponse> items = getOrderItemResponse(order.getOrderItems());

        GetOneOrderResponse orderResponse = orderMapper.orderToGetOneOrderResponse(order);

        return RestResponse.ok(orderResponse);
    }

    private List<GetListOrderItemResponse> getOrderItemResponse(Set<OrderItem> orderItems) {
        List<GetListOrderItemResponse> orderItemResponses = orderItemMapper.setOrderItemToListGetListOrderItemResponse(orderItems);
        orderItemResponses.forEach(x -> {

        });
        return orderItemResponses;
    }

    @Override
    public RestResponse<CreateOrderResponse> createOrder(CreateOrderRequest request) {
        return null;
    }

    @Override
    public void updateOrder(UpdateOrderRequest request) {

    }

    @Override
    public void completePaypalOrder(CompletePaypalOrderRequest request) {

    }
}
