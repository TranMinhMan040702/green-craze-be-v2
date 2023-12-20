package vn.com.greencraze.product.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.amqp.RabbitMQMessageProducer;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.enumeration.NotificationType;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.config.property.RabbitMQProperties;
import vn.com.greencraze.product.dto.request.sale.CreateSaleRequest;
import vn.com.greencraze.product.dto.request.sale.UpdateSaleRequest;
import vn.com.greencraze.product.dto.response.sale.CreateSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetListSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetSaleLatestResponse;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.ProductCategory;
import vn.com.greencraze.product.entity.Sale;
import vn.com.greencraze.product.entity.Variant;
import vn.com.greencraze.product.enumeration.SaleStatus;
import vn.com.greencraze.product.exception.SaleActiveException;
import vn.com.greencraze.product.exception.SaleDateException;
import vn.com.greencraze.product.exception.SaleExpiredException;
import vn.com.greencraze.product.exception.SaleInactiveException;
import vn.com.greencraze.product.mapper.SaleMapper;
import vn.com.greencraze.product.rabbitmq.dto.request.CreateNotificationRequest;
import vn.com.greencraze.product.repository.ProductCategoryRepository;
import vn.com.greencraze.product.repository.SaleRepository;
import vn.com.greencraze.product.repository.specification.SaleSpecification;
import vn.com.greencraze.product.service.ISaleService;
import vn.com.greencraze.product.service.IUploadService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements ISaleService {

    private final SaleRepository saleRepository;
    private final ProductCategoryRepository productCategoryRepository;

    private final IUploadService uploadService;

    private final SaleMapper saleMapper;

    private final RabbitMQMessageProducer producer;

    private final RabbitMQProperties rabbitMQProperties;

    private static final String RESOURCE_NAME = "Sale";
    private static final List<String> SEARCH_FIELDS = List.of("name");

    @Override
    public RestResponse<ListResponse<GetListSaleResponse>> getListSale(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all) {
        SaleSpecification saleSpecification = new SaleSpecification();
        Specification<Sale> sortable = saleSpecification.sortable(isSortAscending, columnName);
        Specification<Sale> searchable = saleSpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListSaleResponse> responses = saleRepository
                .findAll(sortable.and(searchable), pageable)
                .map(saleMapper::saleToGetListSaleResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneSaleResponse> getOneSale(Long id) {
        return saleRepository.findById(id)
                .map(saleMapper::saleToGetOneSaleResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<CreateSaleResponse> createSale(CreateSaleRequest request) {
        if (request.startDate().compareTo(request.endDate()) > 0) {
            throw new SaleDateException("Start date is greater than end date");
        }
        Sale sale = saleMapper.createSaleRequestToSale(request);
        List<ProductCategory> productCategories = request.allProductCategory() ?
                productCategoryRepository.findAll() :
                request.categoryIds().stream()
                        .map(categoryId -> productCategoryRepository.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", "id", categoryId)))
                        .toList();
        sale.setStatus(SaleStatus.INACTIVE);
        sale.setProductCategories(new HashSet<>(productCategories));
        sale.setImage(uploadService.uploadFile(request.image()));
        saleRepository.save(sale);
        return RestResponse.created(saleMapper.saleToCreateSaleResponse(sale));
    }

    @Override
    public void updateSale(Long id, UpdateSaleRequest request) {
        if (request.startDate().compareTo(request.endDate()) > 0) {
            throw new SaleDateException("Start date is greater than end date");
        }

        Sale sale = saleRepository.findById(id)
                .map(s -> saleMapper.updateSaleFromUpdateSaleRequest(s, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        if (request.image() != null) {
            sale.setImage(uploadService.uploadFile(request.image()));
        }

        List<ProductCategory> productCategories = request.allProductCategory() ?
                productCategoryRepository.findAll() :
                request.categoryIds().stream()
                        .map(categoryId -> productCategoryRepository.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", "id", categoryId)))
                        .toList();
        sale.setProductCategories(new HashSet<>(productCategories));
        saleRepository.save(sale);
    }

    @Override
    public void deleteOneSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        sale.setStatus(SaleStatus.INACTIVE);
        saleRepository.save(sale);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void deleteListSale(List<Long> ids) {
        for (Long id : ids) {
            Sale sale = saleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            sale.setStatus(SaleStatus.INACTIVE);
            saleRepository.save(sale);
        }
    }

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

    @Override
    public GetSaleLatestResponse getSaleLatest() {
        Optional<Sale> sale = saleRepository.findByStatus(SaleStatus.ACTIVE);
        return sale.map(saleMapper::saleToGetSaleLatestResponse).orElse(null);
    }

}
