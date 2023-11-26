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
import vn.com.greencraze.commons.exception.InvalidRequestException;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.order.constant.OrderConstants;
import vn.com.greencraze.order.dto.request.order.CompletePaypalOrderRequest;
import vn.com.greencraze.order.dto.request.order.CreateOrderItemRequest;
import vn.com.greencraze.order.dto.request.order.CreateOrderRequest;
import vn.com.greencraze.order.dto.request.order.UpdateOrderRequest;
import vn.com.greencraze.order.dto.response.order.CreateOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderItemResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderResponse;
import vn.com.greencraze.order.entity.Delivery;
import vn.com.greencraze.order.entity.Order;
import vn.com.greencraze.order.entity.OrderItem;
import vn.com.greencraze.order.entity.PaymentMethod;
import vn.com.greencraze.order.entity.Transaction;
import vn.com.greencraze.order.enumeration.OrderStatus;
import vn.com.greencraze.order.enumeration.PaymentCode;
import vn.com.greencraze.order.mapper.OrderItemMapper;
import vn.com.greencraze.order.mapper.OrderMapper;
import vn.com.greencraze.order.repository.DeliveryRepository;
import vn.com.greencraze.order.repository.OrderCancelReasonRepository;
import vn.com.greencraze.order.repository.OrderItemRepository;
import vn.com.greencraze.order.repository.OrderRepository;
import vn.com.greencraze.order.repository.PaymentMethodRepository;
import vn.com.greencraze.order.repository.specification.OrderSpecification;
import vn.com.greencraze.order.service.IOrderService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderCancelReasonRepository orderCancelReasonRepository;

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
    public RestResponse<List<GetListOrderResponse>> getTop5OrderLatest() {
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
        String userId = "";
        Order order = orderRepository
                .findByUserIdAndCode(userId, code)
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

    private Order initOrder(CreateOrderRequest request) {
        String userId = "";
        // get default address
        Long addressId = 1L;
        Delivery delivery = deliveryRepository
                .findById(request.deliveryId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "deliveryId", request.deliveryId()));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(request.paymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "paymentMethodId", request.paymentMethodId()));

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.valueOf(0);

        for (CreateOrderItemRequest oi : request.items()) {
            // get variant from product service
            // Variant

            OrderItem orderItem = orderItemMapper.createOrderItemRequestToOrderItem(oi);
            //            orderItem.setUnitPrice();
            //            orderItem.setTotalPrice();
            //            totalAmount = totalAmount.add();
            orderItems.add(orderItem);
        }
        BigDecimal tax = totalAmount.multiply(OrderConstants.ORDER_TAX);
        totalAmount = totalAmount.add(delivery.getPrice()).add(tax);

        Transaction transaction = new Transaction();
        transaction.setPaymentMethod(paymentMethod.getCode());
        transaction.setTotalPay(totalAmount);

        Order order = new Order();
        //        order.setCode();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setDeliveryMethod(delivery.getName());
        order.setTransaction(transaction);
        order.setNote(request.note());
        order.setOrderItems(orderItems);
        order.setShippingCost(delivery.getPrice());
        order.setTax(OrderConstants.ORDER_TAX.doubleValue());
        order.setStatus(OrderStatus.NOT_PROCESSED);
        order.setPaymentStatus(false);
        order.setTotalAmount(totalAmount);

        return order;
    }

    private void updateProduct() {

    }

    private void updateUserCart() {

    }

    private void createDocket() {

    }

    private void createNotify() {

    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public RestResponse<CreateOrderResponse> createOrder(CreateOrderRequest request) {

        Order order = initOrder(request);

        orderRepository.save(order);

        // pub message to update product in product service
        updateProduct();

        // pub message to update user's cart in user service
        updateUserCart();

        //pub message to create docket in inventory service
        createDocket();

        // pub message to notify in infrastructure service
        createNotify();

        return RestResponse.ok(orderMapper.orderToCreateOrderResponse(order));
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void updateOrder(Long id, UpdateOrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        // check if current user is not in admin role --> find by userid and id --> in case user want to cancel order

        if (order.getStatus() == OrderStatus.NOT_PROCESSED
                && !order.getPaymentStatus()
                && Objects.equals(order.getTransaction().getPaymentMethod(), PaymentCode.PAYPAL.name())) {
            throw new InvalidRequestException("Cannot update this order status, until it was paid by user through PayPal");
        }

        // Cannot update order while it's delivered
        if (order.getStatus() == OrderStatus.DELIVERED)
            throw new InvalidRequestException("Unexpected order status, cannot update order while it's delivered");

        if (order.getStatus() == OrderStatus.CANCELLED
            /*&& (!_currentUserService.IsInRole(USER_ROLE.ADMIN) && !_currentUserService.IsInRole(USER_ROLE.STAFF))*/)
            throw new InvalidRequestException("Unexpected order status, user cannot update order while it's cancelled");

        // Customer cannot cancel order while it's processing, only admin and staff can do that
        if (order.getStatus() != OrderStatus.NOT_PROCESSED
                && Objects.equals(request.status(), OrderStatus.CANCELLED)
            /*&& (!_currentUserService.IsInRole("ADMIN") && !_currentUserService.IsInRole("STAFF"))*/)
            throw new InvalidRequestException("Unexpected order status, cannot cancel order while it's processing");

        Instant now = Instant.now();
        order.setStatus(request.status());
        if (Objects.equals(request.status(), OrderStatus.DELIVERED)) {
            order.setPaymentStatus(true);
            if (Objects.equals(order.getTransaction().getPaymentMethod(), PaymentCode.COD.name()))
                order.getTransaction().setPaidAt(now);
            order.getTransaction().setCompletedAt(now);
        } else if (Objects.equals(request.status(), OrderStatus.CANCELLED)) {
            if (request.otherCancellation().isEmpty() || request.otherCancellation().isBlank()) {
                var cancellationReason = orderCancelReasonRepository.findById(request.orderCancellationReasonId())
                        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cancelReason", request.orderCancellationReasonId()));
                order.setCancelReason(cancellationReason);
            } else {
                order.setOtherCancelReason(request.otherCancellation());
            }
            // update product when cancelling order
            updateProduct();
        } else {
            order.setCancelReason(null);
            order.setOtherCancelReason(null);
        }

        orderRepository.save(order);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void completePaypalOrder(Long id, CompletePaypalOrderRequest request) {
        String userId = "";
        Order order = orderRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        Instant now = Instant.now();

        order.getTransaction().setPaidAt(now);
        order.getTransaction().setPaypalOrderId(request.paypalOrderId());
        order.getTransaction().setPaypalOrderStatus(request.paypalOrderStatus());
        order.setPaymentStatus(true);
        order.setStatus(OrderStatus.PROCESSING);

        orderRepository.save(order);

        //pub message to create notify
        createNotify();
    }

}
