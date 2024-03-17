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
import vn.com.greencraze.commons.auth.AuthFacade;
import vn.com.greencraze.commons.domain.aggreate.CreateOrderAggregate;
import vn.com.greencraze.commons.domain.dto.CreateNotificationRequest;
import vn.com.greencraze.commons.domain.dto.SendEmailRequest;
import vn.com.greencraze.commons.enumeration.EmailEvent;
import vn.com.greencraze.commons.enumeration.NotificationType;
import vn.com.greencraze.commons.enumeration.OrderStatus;
import vn.com.greencraze.commons.exception.InvalidRequestException;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.order.client.address.AddressServiceClient;
import vn.com.greencraze.order.client.address.dto.response.GetOneAddressResponse;
import vn.com.greencraze.order.client.inventory.InventoryServiceClient;
import vn.com.greencraze.order.client.inventory.dto.request.CreateDocketRequest;
import vn.com.greencraze.order.client.product.ProductServiceClient;
import vn.com.greencraze.order.client.product.dto.request.UpdateListProductQuantityRequest;
import vn.com.greencraze.order.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.order.client.product.dto.response.GetOneVariantResponse;
import vn.com.greencraze.order.client.user.UserServiceClient;
import vn.com.greencraze.order.client.user.dto.request.UpdateUserCartRequest;
import vn.com.greencraze.order.client.user.dto.response.GetOneUserResponse;
import vn.com.greencraze.order.client.user.dto.response.GetOrderReviewResponse;
import vn.com.greencraze.order.constant.OrderConstants;
import vn.com.greencraze.order.cronjob.JobManager;
import vn.com.greencraze.order.dto.request.order.CompletePaypalOrderRequest;
import vn.com.greencraze.order.dto.request.order.CreateOrderItemRequest;
import vn.com.greencraze.order.dto.request.order.CreateOrderRequest;
import vn.com.greencraze.order.dto.request.order.UpdateOrderRequest;
import vn.com.greencraze.order.dto.response.order.CreateOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderItemResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderItemResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetTop5OrderLatestResponse;
import vn.com.greencraze.order.entity.Delivery;
import vn.com.greencraze.order.entity.Order;
import vn.com.greencraze.order.entity.OrderItem;
import vn.com.greencraze.order.entity.PaymentMethod;
import vn.com.greencraze.order.entity.Transaction;
import vn.com.greencraze.order.entity.query.OrderItemQuery;
import vn.com.greencraze.order.enumeration.PaymentCode;
import vn.com.greencraze.order.mapper.OrderItemMapper;
import vn.com.greencraze.order.mapper.OrderMapper;
import vn.com.greencraze.order.producer.KafkaProducer;
import vn.com.greencraze.order.repository.DeliveryRepository;
import vn.com.greencraze.order.repository.OrderCancelReasonRepository;
import vn.com.greencraze.order.repository.OrderItemRepository;
import vn.com.greencraze.order.repository.OrderRepository;
import vn.com.greencraze.order.repository.PaymentMethodRepository;
import vn.com.greencraze.order.repository.TransactionRepository;
import vn.com.greencraze.order.repository.specification.OrderSpecification;
import vn.com.greencraze.order.saga.createorder.CreateOrderSagaStateMachineManager;
import vn.com.greencraze.order.service.IOrderService;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderCancelReasonRepository orderCancelReasonRepository;
    private final TransactionRepository transactionRepository;
    private final OrderItemRepository orderItemRepository;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    private final AddressServiceClient addressServiceClient;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    private final KafkaProducer kafkaProducer;

    private final AuthFacade authFacade;

    private final JobManager jobManager;

    private final CreateOrderSagaStateMachineManager stateMachineManager;

    private static final String RESOURCE_NAME = "Order";
    private static final List<String> SEARCH_FIELDS = List.of("code");

    @Override
    public RestResponse<ListResponse<GetListOrderResponse>> getListOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, OrderStatus status) {
        OrderSpecification orderSpecification = new OrderSpecification();
        Specification<Order> sortable = orderSpecification.sortable(isSortAscending, columnName);
        Specification<Order> searchable = orderSpecification.searchable(SEARCH_FIELDS, search);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);

        Page<GetListOrderResponse> orders = orderRepository
                .findAll(sortable.and(searchable), pageable)
                .map(this::mapOrderToGetListOrderResponse);

        return RestResponse.ok(ListResponse.of(orders));
    }

    @Override
    public RestResponse<ListResponse<GetListOrderResponse>> getListUserOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, OrderStatus status) {
        String userId = authFacade.getUserId();

        OrderSpecification orderSpecification = new OrderSpecification();
        Specification<Order> sortable = orderSpecification.sortable(isSortAscending, columnName);
        Specification<Order> searchable = orderSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Order> filterableByStatus = orderSpecification.filterableByStatus(status);
        Specification<Order> filterableByUser = orderSpecification.filterableByUser(userId);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);

        Page<GetListOrderResponse> orders = orderRepository
                .findAll(sortable.and(searchable).and(filterableByStatus).and(filterableByUser), pageable)
                .map(this::mapOrderToGetListOrderResponse);

        return RestResponse.ok(ListResponse.of(orders));
    }

    @Override
    public List<GetTop5OrderLatestResponse> getTop5OrderLatest() {
        OrderSpecification orderSpecification = new OrderSpecification();
        Specification<Order> sortable = orderSpecification.sortable(false, "createdAt");
        Pageable pageable = PageRequest.of(0, 5);

        return orderRepository
                .findAll(sortable, pageable)
                .map(orderMapper::orderToGetTop5OrderLatestResponse)
                .getContent();
    }

    private GetListOrderResponse mapOrderToGetListOrderResponse(Order order) {
        GetListOrderResponse orderResponse = orderMapper.orderToGetListOrderResponse(order);

        RestResponse<GetOneUserResponse> user = userServiceClient.getOneUser(order.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "userId", order.getUserId());
        }

        RestResponse<GetOneAddressResponse> address = addressServiceClient.getOneAddress(order.getAddressId());
        if (address == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "addressId", order.getAddressId());
        }

        return orderResponse.withUser(user.data())
                .withAddress(address.data())
                .withItems(getOrderItemResponse(order.getOrderItems()));
    }

    private GetOneOrderResponse mapOrderToGetOneOrderResponse(Order order) {
        GetOneOrderResponse orderResponse = orderMapper.orderToGetOneOrderResponse(order);

        RestResponse<GetOneUserResponse> user = userServiceClient.getOneUser(order.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "userId", order.getUserId());
        }

        RestResponse<GetOneAddressResponse> address = addressServiceClient.getOneAddress(order.getAddressId());
        if (address == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "addressId", order.getAddressId());
        }
        return orderResponse.withUser(user.data())
                .withAddress(address.data())
                .withItems(getOrderItemResponse(order.getOrderItems()));
    }

    @Override
    public RestResponse<GetOneOrderResponse> getOneOrder(Long id) {
        GetOneOrderResponse order = orderRepository
                .findById(id)
                .map(this::mapOrderToGetOneOrderResponse)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Order", id));
        return RestResponse.ok(order);
    }

    @Override
    public RestResponse<GetOneOrderResponse> getOneOrderByCode(String code) {
        String userId = authFacade.getUserId();
        GetOneOrderResponse order = orderRepository
                .findByUserIdAndCode(userId, code)
                .map(this::mapOrderToGetOneOrderResponse)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Order", code));

        List<Long> orderItemIds = order.items().stream().map(GetListOrderItemResponse::id).toList();
        RestResponse<GetOrderReviewResponse> review = userServiceClient.getOrderReview(orderItemIds);

        if (review.data() == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "orderIds",
                    order.items().stream().map(GetListOrderItemResponse::id).toList());
        }

        if (review.data().reviewedDate() != null) {
            order = order.withReviewedDate(review.data().reviewedDate());
        }
        if (review.data().isReview() != null) {
            order = order.withIsReview(review.data().isReview());
        }

        return RestResponse.ok(order);
    }

    private List<GetListOrderItemResponse> getOrderItemResponse(Set<OrderItem> orderItems) {
        List<GetListOrderItemResponse> updatedOrderItemResponses = new ArrayList<>();
        List<GetListOrderItemResponse> orderItemResponses = orderItemMapper
                .setOrderItemToListGetListOrderItemResponse(orderItems);

        for (GetListOrderItemResponse item : orderItemResponses) {
            RestResponse<GetOneVariantResponse> variantResp = productServiceClient.getOneVariant(item.variantId());
            if (variantResp == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", item.variantId());
            }
            GetOneVariantResponse variant = variantResp.data();

            RestResponse<GetOneProductResponse> productResp = productServiceClient.getOneProduct(variant.productId());
            if (productResp == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "productId", variant.productId());
            }
            GetOneProductResponse product = productResp.data();

            updatedOrderItemResponses.add(item.withSku(variant.sku())
                    .withProductId(variant.productId())
                    .withProductSlug(product.slug())
                    .withProductUnit(product.unit().name())
                    .withProductImage(product.images().stream()
                            .findFirst()
                            .map(GetOneProductResponse.ProductImageResponse::image).orElse(null))
                    .withProductName((product.name()))
                    .withVariantName(variant.name())
                    .withVariantQuantity(Long.valueOf(variant.quantity())));
        }

        return updatedOrderItemResponses;
    }

    private Order initOrder(CreateOrderRequest request) {
        String userId = authFacade.getUserId();

        // get chosen address
        RestResponse<GetOneAddressResponse> address = addressServiceClient.getOneAddress(request.addressId());
        if (address == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "addressId", userId);
        }
        Long addressId = address.data().id();

        Delivery delivery = deliveryRepository
                .findById(request.deliveryId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "deliveryId", request.deliveryId()));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(request.paymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        RESOURCE_NAME, "paymentMethodId", request.paymentMethodId()));

        BigDecimal totalAmount = BigDecimal.ZERO;
        Order order = Order.builder()
                .code(UUID.randomUUID().toString().replace("-", "").toUpperCase()) // TODO: generate code
                .userId(userId)
                .addressId(addressId)
                .deliveryMethod(delivery.getName())
                .note(request.note())
                .shippingCost(delivery.getPrice())
                .tax(OrderConstants.ORDER_TAX.doubleValue())
                .status(OrderStatus.NOT_PROCESSED)
                .paymentStatus(false)
                .build();

        for (CreateOrderItemRequest oi : request.items()) {
            // get variant from product service
            RestResponse<GetOneVariantResponse> variantResp = productServiceClient.getOneVariant(oi.variantId());
            if (variantResp == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", oi.variantId());
            }
            GetOneVariantResponse variant = variantResp.data();

            BigDecimal variantPrice = variant.totalPrice().multiply(BigDecimal.valueOf(oi.quantity()));

            OrderItem orderItem = orderItemMapper.createOrderItemRequestToOrderItem(oi);
            orderItem.setOrder(order);
            orderItem.setUnitPrice(variant.totalPrice());
            orderItem.setTotalPrice(variantPrice);

            totalAmount = totalAmount.add(variantPrice);

            order.getOrderItems().add(orderItem);
        }

        BigDecimal tax = totalAmount.multiply(OrderConstants.ORDER_TAX);
        totalAmount = totalAmount.add(delivery.getPrice()).add(tax);
        order.setTotalAmount(totalAmount);

        Transaction transaction = new Transaction();
        transaction.setPaymentMethod(paymentMethod.getCode());
        transaction.setTotalPay(totalAmount);
        transaction.setOrder(order);
        order.setTransaction(transaction);

        orderRepository.save(order);
        transactionRepository.save(transaction);

        return order;
    }

    @Deprecated
    private void updateProduct(List<CreateOrderItemRequest> items) {
        UpdateListProductQuantityRequest request = new UpdateListProductQuantityRequest(new ArrayList<>());

        for (CreateOrderItemRequest item : items) {

            RestResponse<GetOneVariantResponse> variantResp = productServiceClient.getOneVariant(item.variantId());
            if (variantResp == null)
                throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", item.variantId());
            GetOneVariantResponse variant = variantResp.data();

            request.quantityItems().add(new UpdateListProductQuantityRequest.ProductQuantityItem(
                    variant.productId(), variant.quantity() * item.quantity()));
        }

        productServiceClient.updateProductQuantity(request);
    }

    @Deprecated
    private void updateUserCart(List<CreateOrderItemRequest> items) {
        String userId = authFacade.getUserId();

        List<Long> variantIds = new ArrayList<>();
        for (CreateOrderItemRequest item : items) {
            variantIds.add(item.variantId());
        }

        UpdateUserCartRequest request = new UpdateUserCartRequest(userId, variantIds);

        userServiceClient.updateUserCart(request);
    }

    @Deprecated
    private void createDocket(List<CreateOrderItemRequest> items, Long orderId, String type) {
        CreateDocketRequest request = new CreateDocketRequest(orderId, type, new ArrayList<>());

        for (CreateOrderItemRequest item : items) {
            RestResponse<GetOneVariantResponse> variantResp = productServiceClient.getOneVariant(item.variantId());
            if (variantResp == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", item.variantId());
            }
            GetOneVariantResponse variant = variantResp.data();

            request.productDockets().add(new CreateDocketRequest.ProductDocket(
                    variant.productId(), variant.quantity() * item.quantity()));
        }

        inventoryServiceClient.createDocket(request);
    }

    private void createNotify(Order order) {
        GetOneProductResponse productResponse = productServiceClient.getOneProductByVariant(
                Objects.requireNonNull(order.getOrderItems().stream()
                                .findFirst()
                                .orElse(null))
                        .getVariantId()).data();

        CreateNotificationRequest req = CreateNotificationRequest.builder()
                .userId(order.getUserId())
                .type(NotificationType.ORDER)
                .content(String.format("Đơn hàng #%s của bạn đang được chúng tôi xử lý",
                        order.getCode()))
                .title("Đặt hàng thành công")
                .anchor("/user/order/" + order.getCode())
                .image(Objects.requireNonNull(productResponse.images().stream()
                                .findFirst()
                                .orElse(null))
                        .image())
                .build();
        if (Objects.equals(order.getTransaction().getPaymentMethod(), PaymentCode.PAYPAL.name())) {
            req = req.withContent(String.format("Đơn hàng #%s của bạn cần phải thanh toán qua Paypal, " +
                                    "vui lòng thanh toán để đơn hàng được xử lý",
                            order.getCode()))
                    .withTitle("Thanh toán đơn hàng")
                    .withAnchor("/checkout/payment/" + order.getCode());
        } else {
            String userId = authFacade.getUserId();
            RestResponse<GetOneUserResponse> userResp = userServiceClient.getOneUser(userId);
            if (userResp == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "userId", userId);
            }
            GetOneUserResponse user = userResp.data();

            RestResponse<GetOneAddressResponse> addressResp = addressServiceClient.getDefaultAddress(userId);
            if (addressResp == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "userId", userId);
            }
            GetOneAddressResponse address = addressResp.data();
            String addressDetail = address.street() + ", " + address.ward().name()
                    + ", " + address.district().name() + ", " + address.province().name();

            // TODO: refactor Locale
            NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

            // send email order
            kafkaProducer.sendMail(order.getId().toString(), SendEmailRequest.builder()
                    .event(EmailEvent.ORDER_CONFIRMATION)
                    .email(user.email())
                    .payload(Map.of(
                            "name", user.firstName() + " " + user.lastName(),
                            "email", address.email(),
                            "receiver", address.receiver(),
                            "phone", address.phone(),
                            "totalPrice", numberFormat.format(order.getTotalAmount()),
                            "paymentMethod", order.getTransaction().getPaymentMethod(),
                            "address", addressDetail
                    ))
                    .build());

        }
        kafkaProducer.sendNotification(order.getId().toString(), req);
    }

    private boolean hasInRole(String role) {
        return authFacade.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }


    @Override
    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    public RestResponse<CreateOrderResponse> createOrder(CreateOrderRequest request) {
        Order order = initOrder(request);

        // map order to createOrderAggregate
        CreateOrderAggregate aggregate = CreateOrderAggregate.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(order.getOrderItems().stream().map(item -> CreateOrderAggregate.OrderItemAggregate.builder()
                                .id(item.getId())
                                .variantId(item.getVariantId())
                                .quantity(item.getQuantity())
                                .build())
                        .toList())
                .build();

        // create statemachine
        stateMachineManager.putSaga(aggregate);

        try {
            createNotify(order);
        } catch (Exception ignored) {
        }

        // auto cancel order after 10p
        jobManager.execute(order.getId());

        return RestResponse.ok(orderMapper.orderToCreateOrderResponse(order));
    }

    private void validateOrderStatus(Order order, OrderStatus status) {
        // Cannot update order while it's not paid by user through PayPal
        if (order.getStatus() == OrderStatus.NOT_PROCESSED
                && !order.getPaymentStatus()
                && status != OrderStatus.CANCELLED
                && Objects.equals(order.getTransaction().getPaymentMethod(), PaymentCode.PAYPAL.name())) {
            throw new InvalidRequestException(
                    "Cannot update this order status, it was not paid by user through PayPal"
            );
        }

        //  Cannot cancel order while it's paid
        if (status == OrderStatus.CANCELLED
                && order.getPaymentStatus()) {
            throw new InvalidRequestException(
                    "Cannot update this order status, it was paid by user before"
            );
        }

        // Cannot update order while it's delivered
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidRequestException(
                    "Unexpected order status, cannot update order while it's delivered"
            );
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidRequestException(
                    "Unexpected order status, order was cancelled"
            );
        }

        // Customer cannot cancel order while it's processing, only admin and staff can do that
        if (order.getStatus() != OrderStatus.NOT_PROCESSED
                && Objects.equals(status, OrderStatus.CANCELLED)
                && !(hasInRole("ROLE_ADMIN") || hasInRole("ROLE_STAFF"))) {
            throw new InvalidRequestException("Unexpected order status, cannot cancel order while it's processing");
        }
    }

    private void handleChangeStatus(Order order, UpdateOrderRequest request) {
        Instant now = Instant.now();
        order.setStatus(request.status());
        if (Objects.equals(request.status(), OrderStatus.DELIVERED)) {
            order.setPaymentStatus(true);
            if (Objects.equals(order.getTransaction().getPaymentMethod(), PaymentCode.COD.name()))
                order.getTransaction().setPaidAt(now);
            order.getTransaction().setCompletedAt(now);
        } else if (Objects.equals(request.status(), OrderStatus.CANCELLED)) {
            if (request.orderCancellationReasonId() != null) {

                var cancellationReason = orderCancelReasonRepository.findById(request.orderCancellationReasonId())
                        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cancelReason",
                                request.orderCancellationReasonId()));
                order.setCancelReason(cancellationReason);

            } else if (request.otherCancellation() != null && !request.otherCancellation().isBlank()) {
                order.setOtherCancelReason(request.otherCancellation());
            }
            // update product when cancelling order
            List<CreateOrderItemRequest> items = orderItemMapper
                    .orderItemToCreateOrderItemRequest(order.getOrderItems())
                    .stream().map(x -> x.withQuantity(x.quantity() * -1)).toList();
            updateProduct(items);

            // TODO: create docket when cancelling order
            createDocket(orderItemMapper.orderItemToCreateOrderItemRequest(
                    order.getOrderItems()), order.getId(), "IMPORT");
        } else {
            order.setCancelReason(null);
            order.setOtherCancelReason(null);
        }
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void updateOrder(Long id, UpdateOrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        // check if current user is not in admin role --> find by userid and id --> in case user want to cancel order
        if (!hasInRole("ROLE_ADMIN")) {
            String userId = authFacade.getUserId();
            order = orderRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        }

        validateOrderStatus(order, request.status());

        handleChangeStatus(order, request);

        orderRepository.save(order);

        try {
            GetOneProductResponse productResponse = productServiceClient.getOneProductByVariant(
                    Objects.requireNonNull(order.getOrderItems().stream()
                                    .findFirst()
                                    .orElse(null))
                            .getVariantId()).data();

            CreateNotificationRequest req = CreateNotificationRequest.builder()
                    .userId(order.getUserId())
                    .type(NotificationType.ORDER)
                    .content(String.format("Đơn hàng #%s của bạn đã chuyển sang trạng thái %s",
                            order.getCode(), order.getStatus().name()))
                    .title("Cập nhật trạng thái đơn hàng")
                    .anchor("/user/order/" + order.getCode())
                    .image(Objects.requireNonNull(productResponse.images().stream()
                                    .findFirst()
                                    .orElse(null))
                            .image())
                    .build();

            kafkaProducer.sendNotification(order.getId().toString(), req);
        } catch (Exception ignored) {
        }
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void completePaypalOrder(Long id, CompletePaypalOrderRequest request) {
        String userId = authFacade.getUserId();
        Order order = orderRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        Instant now = Instant.now();

        order.getTransaction().setPaidAt(now);
        order.getTransaction().setPaypalOrderId(request.paypalOrderId());
        order.getTransaction().setPaypalOrderStatus(request.paypalOrderStatus());
        order.setPaymentStatus(true);
        order.setStatus(OrderStatus.PROCESSING);

        orderRepository.save(order);

        try {
            RestResponse<GetOneUserResponse> userResp = userServiceClient.getOneUser(userId);
            if (userResp == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "userId", userId);
            }
            GetOneUserResponse user = userResp.data();

            RestResponse<GetOneAddressResponse> addressResp = addressServiceClient.getDefaultAddress(userId);
            if (addressResp == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "userId", userId);
            }
            GetOneAddressResponse address = addressResp.data();
            // TODO: refactor format String
            String addressDetail = address.street() + ", " + address.ward().name()
                    + ", " + address.district().name() + ", " + address.province().name();

            // TODO: refactor replace Locale
            NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

            // send email order
            kafkaProducer.sendMail(order.getId().toString(), SendEmailRequest.builder()
                    .event(EmailEvent.ORDER_CONFIRMATION)
                    .email(user.email())
                    .payload(Map.of(
                            "name", user.firstName() + " " + user.lastName(),
                            "email", address.email(),
                            "receiver", address.receiver(),
                            "phone", address.phone(),
                            "totalPrice", numberFormat.format(order.getTotalAmount()),
                            "paymentMethod", order.getTransaction().getPaymentMethod(),
                            "address", addressDetail
                    ))
                    .build());
            GetOneProductResponse productResponse = productServiceClient.getOneProductByVariant(
                    Objects.requireNonNull(order.getOrderItems().stream()
                                    .findFirst()
                                    .orElse(null))
                            .getVariantId()).data();

            kafkaProducer.sendNotification(order.getId().toString(), CreateNotificationRequest.builder()
                    .userId(order.getUserId())
                    .type(NotificationType.ORDER)
                    .content(String.format("Đơn hàng #%s của bạn đã thanh toán thành công và đang được chúng tôi xử lý",
                            order.getCode()))
                    .title("Thanh toán thành công")
                    .anchor("/user/order/" + order.getCode())
                    .image(Objects.requireNonNull(productResponse.images().stream()
                                    .findFirst()
                                    .orElse(null))
                            .image())
                    .build());
        } catch (Exception ignored) {
        }
    }

    // call from other service
    @Override
    public RestResponse<GetOneOrderItemResponse> getOneOrderItem(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .map(orderItemMapper::orderItemToGetOneOrderItemResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "orderItemId", orderItemId));
    }

    @Override
    public Long getTotalOrderWithStatusDelivered() {
        return orderRepository.countByStatus(OrderStatus.DELIVERED);
    }

    @Override
    public Map<String, Long> getTopSellingProduct(Instant startDate, Instant endDate) {
        Map<String, Long> productWithQuantity = new HashMap<>();
        Map<String, List<Long>> data = productServiceClient.getListProductWithVariant();

        List<OrderItemQuery> orderItemQueries = orderItemRepository.findAllByCreatedAt(startDate, endDate);

        orderItemQueries.forEach(orderItemQuery ->
                data.entrySet().stream()
                        .filter(entry -> entry.getValue() != null
                                && entry.getValue().contains(orderItemQuery.getVariantId()))
                        .findFirst()
                        .ifPresent(entry -> productWithQuantity.merge(entry.getKey(),
                                orderItemQuery.getTotalQuantity(), Long::sum)));

        if (productWithQuantity.size() < 5) {
            data.entrySet().stream()
                    .filter(entry -> !productWithQuantity.containsKey(entry.getKey()))
                    .limit(5 - productWithQuantity.size())
                    .forEach(entry -> productWithQuantity.put(entry.getKey(), 0L));
        }

        return productWithQuantity;
    }

    @Override
    public Map<String, Long> getOrderTotalByStatus(Instant startDate, Instant endDate) {
        return OrderStatus.Status().stream()
                .collect(Collectors.toMap(
                        OrderStatus::name,
                        status -> orderRepository.countByStatusAndCreatedAtBetween(status, startDate, endDate)
                ));
    }

    @Override
    public void cancelOrderSaga(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

}
