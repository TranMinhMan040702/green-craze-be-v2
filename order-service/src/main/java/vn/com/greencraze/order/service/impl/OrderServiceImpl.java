package vn.com.greencraze.order.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.auth.AuthFacade;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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

    private final AddressServiceClient addressServiceClient;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    private final AuthFacade authFacade;
    private static final String RESOURCE_NAME = "Order";
    private static final List<String> SEARCH_FIELDS = List.of("code");

    @Override
    public RestResponse<ListResponse<GetListOrderResponse>> getListOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, String status) {
        OrderSpecification orderSpecification = new OrderSpecification();
        Specification<Order> sortable = orderSpecification.sortable(isSortAscending, columnName);
        Specification<Order> searchable = orderSpecification.searchable(SEARCH_FIELDS, search);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, size);

        Page<GetListOrderResponse> orders = orderRepository
                .findAll(sortable.and(searchable), pageable)
                .map(this::mapOrderToGetListOrderResponse);

        return RestResponse.ok(ListResponse.of(orders));
    }

    @Override
    public RestResponse<ListResponse<GetListOrderResponse>> getListUserOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, String status) {
        String userId = authFacade.getUserId();

        OrderSpecification orderSpecification = new OrderSpecification();
        Specification<Order> sortable = orderSpecification.sortable(isSortAscending, columnName);
        Specification<Order> searchable = orderSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Order> filterable = orderSpecification.filterable(userId, status);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, size);

        Page<GetListOrderResponse> orders = orderRepository
                .findAll(sortable.and(searchable).and(filterable), pageable)
                .map(this::mapOrderToGetListOrderResponse);

        return RestResponse.ok(ListResponse.of(orders));
    }

    @Override
    public RestResponse<List<GetListOrderResponse>> getTop5OrderLatest() {
        OrderSpecification orderSpecification = new OrderSpecification();
        Specification<Order> sortable = orderSpecification.sortable(false, "createdAt");
        Pageable pageable = PageRequest.of(1, 5);

        List<GetListOrderResponse> orders = orderRepository
                .findAll(sortable, pageable)
                .map(this::mapOrderToGetListOrderResponse)
                .getContent();

        return RestResponse.ok(orders);
    }

    private GetListOrderResponse mapOrderToGetListOrderResponse(Order order) {
        GetListOrderResponse orderResponse = orderMapper.orderToGetListOrderResponse(order);

        GetOneUserResponse user = userServiceClient.getOneUser(order.getUserId());
        if (user == null)
            throw new ResourceNotFoundException("User", "userId", order.getUserId());

        GetOneAddressResponse address = addressServiceClient.getOneAddress(order.getAddressId());
        if (address == null)
            throw new ResourceNotFoundException("Address", "addressId", order.getAddressId());

        return orderResponse.withUser(user).withAddress(address).withItems(getOrderItemResponse(order.getOrderItems()));
    }

    private GetOneOrderResponse mapOrderToGetOneOrderResponse(Order order) {
        GetOneOrderResponse orderResponse = orderMapper.orderToGetOneOrderResponse(order);

        GetOneUserResponse user = userServiceClient.getOneUser(order.getUserId());
        if (user == null)
            throw new ResourceNotFoundException("User", "userId", order.getUserId());

        GetOneAddressResponse address = addressServiceClient.getOneAddress(order.getAddressId());
        if (address == null)
            throw new ResourceNotFoundException("Address", "addressId", order.getAddressId());

        return orderResponse.withUser(user).withAddress(address).withItems(getOrderItemResponse(order.getOrderItems()));
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
        return RestResponse.ok(order);
    }

    private List<GetListOrderItemResponse> getOrderItemResponse(Set<OrderItem> orderItems) {
        List<GetListOrderItemResponse> updatedOrderItemResponses = new ArrayList<>();
        List<GetListOrderItemResponse> orderItemResponses = orderItemMapper
                .setOrderItemToListGetListOrderItemResponse(orderItems);

        for (GetListOrderItemResponse item : orderItemResponses) {
            GetOneVariantResponse variant = productServiceClient.getOneVariant(item.variantId());
            if (variant == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", item.variantId());
            }

            GetOneProductResponse product = productServiceClient.getOneProduct(variant.productId());
            if (product == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "productId", variant.productId());
            }

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
        Order order = new Order();
        // get default address
        GetOneAddressResponse address = addressServiceClient.getDefaultAddress(userId);
        if (address == null) {
            throw new ResourceNotFoundException("Address", "addressId", userId);
        }
        Long addressId = address.id();

        Delivery delivery = deliveryRepository
                .findById(request.deliveryId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "deliveryId", request.deliveryId()));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(request.paymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        RESOURCE_NAME, "paymentMethodId", request.paymentMethodId()));

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CreateOrderItemRequest oi : request.items()) {
            // get variant from product service
            GetOneVariantResponse variant = productServiceClient.getOneVariant(oi.variantId());
            if (variant == null) {
                throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", oi.variantId());
            }

            BigDecimal variantPrice = variant.totalPrice().multiply(BigDecimal.valueOf(oi.quantity()));

            OrderItem orderItem = orderItemMapper.createOrderItemRequestToOrderItem(oi);
            orderItem.setOrder(order);
            orderItem.setUnitPrice(variant.totalPrice());
            orderItem.setTotalPrice(variantPrice);

            totalAmount = totalAmount.add(variantPrice);

            orderItems.add(orderItem);
        }
        BigDecimal tax = totalAmount.multiply(OrderConstants.ORDER_TAX);
        totalAmount = totalAmount.add(delivery.getPrice()).add(tax);

        Transaction transaction = new Transaction();
        transaction.setPaymentMethod(paymentMethod.getCode());
        transaction.setTotalPay(totalAmount);

        return Order.builder()
                .code(UUID.randomUUID().toString().replace('-', ' ').toUpperCase())
                .userId(userId)
                .addressId(addressId)
                .deliveryMethod(delivery.getName())
                .transaction(transaction)
                .note(request.note())
                .orderItems(orderItems)
                .shippingCost(delivery.getPrice())
                .tax(OrderConstants.ORDER_TAX.doubleValue())
                .status(OrderStatus.NOT_PROCESSED)
                .paymentStatus(false)
                .totalAmount(totalAmount)
                .build();
    }

    private void updateProduct(List<CreateOrderItemRequest> items) {
        UpdateListProductQuantityRequest request = new UpdateListProductQuantityRequest(
                new ArrayList<>()
        );

        for (CreateOrderItemRequest item : items) {

            GetOneVariantResponse variant = productServiceClient.getOneVariant(item.variantId());
            if (variant == null)
                throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", item.variantId());

            request.quantityItems().add(
                    new UpdateListProductQuantityRequest.ProductQuantityItem(
                            variant.productId(),
                            variant.quantity() * item.quantity()
                    )
            );

        }

        productServiceClient.updateProductQuantity(request);
    }

    private void updateUserCart(List<CreateOrderItemRequest> items) {
        String userId = authFacade.getUserId();

        List<Long> variantIds = new ArrayList<>();
        for (CreateOrderItemRequest item : items) {
            variantIds.add(item.variantId());
        }

        UpdateUserCartRequest request = new UpdateUserCartRequest(
                userId,
                variantIds
        );

        userServiceClient.updateUserCart(request);
    }

    private void createDocket(List<CreateOrderItemRequest> items, Long orderId, String type) {
        CreateDocketRequest request = new CreateDocketRequest(orderId, type, new ArrayList<>());

        for (CreateOrderItemRequest item : items) {
            GetOneVariantResponse variant = productServiceClient.getOneVariant(item.variantId());
            if (variant == null)
                throw new ResourceNotFoundException(RESOURCE_NAME, "variantId", item.variantId());

            request.productDockets().add(
                    new CreateDocketRequest.ProductDocket(
                            variant.productId(),
                            variant.quantity() * item.quantity())
            );
        }
        inventoryServiceClient.createDocket(request);
    }

    private void createNotify() {

    }

    private boolean hasInRole(String role) {
        Authentication auth = authFacade.getAuthentication();

        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public RestResponse<CreateOrderResponse> createOrder(CreateOrderRequest request) {

        Order order = initOrder(request);

        orderRepository.save(order);

        // pub message to update product in product service
        updateProduct(request.items());

        // pub message to update user's cart in user service
        updateUserCart(request.items());

        //pub message to create docket in inventory service
        createDocket(request.items(), order.getId(), "EXPORT");

        // pub message to notify in infrastructure service
        createNotify();

        return RestResponse.ok(orderMapper.orderToCreateOrderResponse(order));
    }

    private void validateOrderStatus(Order order, OrderStatus status) {
        if (order.getStatus() == OrderStatus.NOT_PROCESSED
                && !order.getPaymentStatus()
                && Objects.equals(order.getTransaction().getPaymentMethod(), PaymentCode.PAYPAL.name())) {
            throw new InvalidRequestException("Cannot update this order status, until it was paid by user through PayPal");
        }

        // Cannot update order while it's delivered
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidRequestException("Unexpected order status, cannot update order while it's delivered");
        }

        if (order.getStatus() == OrderStatus.CANCELLED && (!hasInRole("ADMIN") && !hasInRole("STAFF"))) {
            throw new InvalidRequestException("Unexpected order status, user cannot update order while it's cancelled");
        }

        // Customer cannot cancel order while it's processing, only admin and staff can do that
        if (order.getStatus() != OrderStatus.NOT_PROCESSED
                && Objects.equals(status, OrderStatus.CANCELLED)
                && (!hasInRole("ADMIN") && !hasInRole("STAFF"))) {
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
            if (request.otherCancellation().isEmpty() || request.otherCancellation().isBlank()) {
                var cancellationReason = orderCancelReasonRepository.findById(request.orderCancellationReasonId())
                        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "cancelReason",
                                request.orderCancellationReasonId()));
                order.setCancelReason(cancellationReason);
            } else {
                order.setOtherCancelReason(request.otherCancellation());
            }
            // update product when cancelling order
            List<CreateOrderItemRequest> items = orderItemMapper.orderItemToCreateOrderItemRequest(order.getOrderItems());
            items.forEach(x -> x = x.withQuantity(x.quantity() * -1));
            updateProduct(items);

            // create docket when cancelling order
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
        if (!hasInRole("ADMIN")) {
            String userId = authFacade.getUserId();
            order = orderRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        }

        validateOrderStatus(order, request.status());

        handleChangeStatus(order, request);

        orderRepository.save(order);
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

        //pub message to create notify
        createNotify();
    }

}
