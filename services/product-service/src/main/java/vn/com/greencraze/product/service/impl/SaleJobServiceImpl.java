package vn.com.greencraze.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.greencraze.amqp.RabbitMQMessageProducer;
import vn.com.greencraze.commons.enumeration.NotificationType;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.config.property.RabbitMQProperties;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.ProductCategory;
import vn.com.greencraze.product.entity.Sale;
import vn.com.greencraze.product.entity.Variant;
import vn.com.greencraze.product.enumeration.SaleStatus;
import vn.com.greencraze.product.exception.SaleActiveException;
import vn.com.greencraze.product.exception.SaleDateException;
import vn.com.greencraze.product.exception.SaleExpiredException;
import vn.com.greencraze.product.exception.SaleInactiveException;
import vn.com.greencraze.product.rabbitmq.dto.request.CreateNotificationRequest;
import vn.com.greencraze.product.repository.SaleRepository;
import vn.com.greencraze.product.service.ISaleJobService;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SaleJobServiceImpl implements ISaleJobService {

    private final SaleRepository saleRepository;

    private final RabbitMQMessageProducer producer;

    private final RabbitMQProperties rabbitMQProperties;


    private static final String RESOURCE_NAME = "Sale";

    @Override
    public void applySale(Long id) {
        if (saleRepository.existsByStatus(SaleStatus.ACTIVE)) {
            throw new SaleActiveException("There is a sale going on");
        }

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        if (sale.getStartDate().compareTo(Instant.now()) > 0 || Instant.now().compareTo(sale.getEndDate()) > 0) {
            throw new SaleDateException("Sale period invalid");
        }

        for (ProductCategory productCategory : sale.getProductCategories()) {
            for (Product product : productCategory.getProducts()) {
                for (Variant variant : product.getVariants()) {
                    variant.setPromotionalItemPrice(variant.getItemPrice().subtract(
                            variant.getItemPrice().multiply(BigDecimal.valueOf(sale.getPromotionalPercent() / 100))));
                    variant.setTotalPromotionalPrice(variant.getPromotionalItemPrice().multiply(
                            BigDecimal.valueOf(variant.getQuantity())));
                }
            }
        }
        sale.setStatus(SaleStatus.ACTIVE);
        saleRepository.save(sale);

        producer.publish(CreateNotificationRequest.builder()
                        .type(NotificationType.SALE)
                        .content(String.format("Đợt khuyến mãi hiện đang có mặt tại cửa hàng, giảm giá lên tới %.1f",
                                sale.getPromotionalPercent()))
                        .title(sale.getName())
                        .anchor("#")
                        .image(sale.getImage())
                        .build(),
                rabbitMQProperties.internalExchange(),
                rabbitMQProperties.notificationRoutingKey());
    }

    @Override
    public void cancelSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        switch (sale.getStatus()) {
            case INACTIVE -> throw new SaleInactiveException();
            case EXPIRED -> throw new SaleExpiredException();
        }

        for (ProductCategory productCategory : sale.getProductCategories()) {
            for (Product product : productCategory.getProducts()) {
                for (Variant variant : product.getVariants()) {
                    variant.setPromotionalItemPrice(null);
                    variant.setTotalPromotionalPrice(null);
                }
            }
        }

        sale.setStatus(SaleStatus.INACTIVE);
        saleRepository.save(sale);
    }

}
