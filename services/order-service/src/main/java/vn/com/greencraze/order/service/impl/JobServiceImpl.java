package vn.com.greencraze.order.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.domain.dto.CreateNotificationRequest;
import vn.com.greencraze.commons.enumeration.NotificationType;
import vn.com.greencraze.commons.enumeration.OrderStatus;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.order.client.inventory.InventoryServiceClient;
import vn.com.greencraze.order.client.inventory.dto.request.CreateDocketRequest;
import vn.com.greencraze.order.client.product.ProductServiceClient;
import vn.com.greencraze.order.client.product.dto.request.UpdateListProductQuantityRequest;
import vn.com.greencraze.order.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.order.client.product.dto.response.GetOneVariantResponse;
import vn.com.greencraze.order.dto.request.order.CreateOrderItemRequest;
import vn.com.greencraze.order.entity.Order;
import vn.com.greencraze.order.enumeration.PaymentCode;
import vn.com.greencraze.order.mapper.OrderItemMapper;
import vn.com.greencraze.order.producer.KafkaProducer;
import vn.com.greencraze.order.repository.OrderRepository;
import vn.com.greencraze.order.service.IJobService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JobServiceImpl implements IJobService {

    private final OrderRepository orderRepository;

    private final OrderItemMapper orderItemMapper;

    private final ProductServiceClient productServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    private final KafkaProducer kafkaProducer;


    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void cancelOrderJob(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        if (!order.getPaymentStatus() && order.getTransaction().getPaymentMethod().equals(PaymentCode.PAYPAL.name())) {
            // update Status Order is CANCELLED
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            // update product's quantity
            List<CreateOrderItemRequest> items = orderItemMapper
                    .orderItemToCreateOrderItemRequest(order.getOrderItems())
                    .stream().map(x -> x.withQuantity(x.quantity() * -1)).toList();
            updateProduct(items);

            // create docket when cancelling order
            createDocket(orderItemMapper.orderItemToCreateOrderItemRequest(
                    order.getOrderItems()), order.getId(), "IMPORT");

            // create notify
            try {
                GetOneProductResponse productResponse = productServiceClient.getOneProductByVariant(
                        Objects.requireNonNull(order.getOrderItems().stream()
                                        .findFirst()
                                        .orElse(null))
                                .getVariantId()).data();

                kafkaProducer.sendNotification(order.getId().toString(), CreateNotificationRequest.builder()
                        .userId(order.getUserId())
                        .type(NotificationType.ORDER)
                        .content(String.format("Đơn hàng #%s của bạn đã bị hủy do quá thời gian thanh toán",
                                order.getCode()))
                        .title("Hủy đơn hàng")
                        .anchor("/user/order/" + order.getCode())
                        .image(Objects.requireNonNull(productResponse.images().stream()
                                        .findFirst()
                                        .orElse(null))
                                .image())
                        .build());
            } catch (Exception ignored) {
            }
        }
    }

    private void updateProduct(List<CreateOrderItemRequest> items) {
        UpdateListProductQuantityRequest request = new UpdateListProductQuantityRequest(new ArrayList<>());
        for (CreateOrderItemRequest item : items) {
            RestResponse<GetOneVariantResponse> variantResp = productServiceClient.getOneVariant(item.variantId());
            if (variantResp == null)
                throw new ResourceNotFoundException("Order", "variantId", item.variantId());
            GetOneVariantResponse variant = variantResp.data();
            request.quantityItems().add(new UpdateListProductQuantityRequest.ProductQuantityItem(
                    variant.productId(), variant.quantity() * item.quantity()));
        }
        productServiceClient.updateProductQuantity(request);
    }

    private void createDocket(List<CreateOrderItemRequest> items, Long orderId, String type) {
        CreateDocketRequest request = new CreateDocketRequest(orderId, type, new ArrayList<>());
        for (CreateOrderItemRequest item : items) {
            RestResponse<GetOneVariantResponse> variantResp = productServiceClient.getOneVariant(item.variantId());
            if (variantResp == null) {
                throw new ResourceNotFoundException("Order", "variantId", item.variantId());
            }
            GetOneVariantResponse variant = variantResp.data();
            request.productDockets().add(new CreateDocketRequest.ProductDocket(
                    variant.productId(), variant.quantity() * item.quantity()));
        }
        inventoryServiceClient.createDocket(request);
    }

}
