package vn.com.greencraze.product.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.backgroundjob.JobManager;
import vn.com.greencraze.product.dto.request.sale.CreateSaleRequest;
import vn.com.greencraze.product.dto.request.sale.UpdateSaleRequest;
import vn.com.greencraze.product.dto.response.sale.CreateSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetListSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetSaleLatestResponse;
import vn.com.greencraze.product.entity.ProductCategory;
import vn.com.greencraze.product.entity.Sale;
import vn.com.greencraze.product.enumeration.SaleStatus;
import vn.com.greencraze.product.exception.SaleDateException;
import vn.com.greencraze.product.mapper.SaleMapper;
import vn.com.greencraze.product.producer.KafkaProducer;
import vn.com.greencraze.product.repository.ProductCategoryRepository;
import vn.com.greencraze.product.repository.SaleRepository;
import vn.com.greencraze.product.repository.specification.SaleSpecification;
import vn.com.greencraze.product.service.ISaleService;
import vn.com.greencraze.product.service.IUploadService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl extends SaleJobServiceImpl implements ISaleService {

    private final SaleRepository saleRepository;
    private final ProductCategoryRepository productCategoryRepository;

    private final IUploadService uploadService;

    private final SaleMapper saleMapper;

    private final JobManager jobManager;

    private static final String RESOURCE_NAME = "Sale";
    private static final List<String> SEARCH_FIELDS = List.of("name");

    public SaleServiceImpl(SaleRepository saleRepository,
                           KafkaProducer kafkaProducer,
                           SaleRepository saleRepository1,
                           ProductCategoryRepository productCategoryRepository,
                           IUploadService uploadService,
                           SaleMapper saleMapper,
                           JobManager jobManager) {
        super(saleRepository, kafkaProducer);
        this.saleRepository = saleRepository1;
        this.productCategoryRepository = productCategoryRepository;
        this.uploadService = uploadService;
        this.saleMapper = saleMapper;
        this.jobManager = jobManager;
    }

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

        jobManager.handleSaleJobExecution(sale.getId());

        return RestResponse.created(saleMapper.saleToCreateSaleResponse(sale));
    }

    @Override
    public void updateSale(Long id, UpdateSaleRequest request) {
        if (request.startDate().compareTo(request.endDate()) > 0) {
            throw new SaleDateException("Start date is greater than end date");
        }

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        boolean isStartDateChanged = sale.getStartDate().compareTo(request.startDate()) != 0;
        boolean isEndDateChanged = sale.getEndDate().compareTo(request.endDate()) != 0;

        sale = saleMapper.updateSaleFromUpdateSaleRequest(sale, request);

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

        if (isStartDateChanged) {
            jobManager.applySaleJobExecution(sale.getId(), request.startDate());
        }

        if (isEndDateChanged) {
            jobManager.cancelSaleJobExecution(sale.getId(), request.endDate());
        }
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
    public GetSaleLatestResponse getSaleLatest() {
        Optional<Sale> sale = saleRepository.findByStatus(SaleStatus.ACTIVE);
        return sale.map(saleMapper::saleToGetSaleLatestResponse).orElse(null);
    }

}
