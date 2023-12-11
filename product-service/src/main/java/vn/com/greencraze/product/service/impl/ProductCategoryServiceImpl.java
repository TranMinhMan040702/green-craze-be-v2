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
import vn.com.greencraze.product.dto.request.category.CreateProductCategoryRequest;
import vn.com.greencraze.product.dto.request.category.UpdateProductCategoryRequest;
import vn.com.greencraze.product.dto.response.category.CreateProductCategoryResponse;
import vn.com.greencraze.product.dto.response.category.GetListProductCategoryResponse;
import vn.com.greencraze.product.dto.response.category.GetOneProductCategoryBySlugResponse;
import vn.com.greencraze.product.dto.response.category.GetOneProductCategoryResponse;
import vn.com.greencraze.product.entity.ProductCategory;
import vn.com.greencraze.product.mapper.ProductCategoryMapper;
import vn.com.greencraze.product.repository.ProductCategoryRepository;
import vn.com.greencraze.product.repository.specification.ProductCategorySpecification;
import vn.com.greencraze.product.service.IProductCategoryService;
import vn.com.greencraze.product.service.IUploadService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements IProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;
    private final IUploadService uploadService;

    private static final String RESOURCE_NAME = "ProductCategory";
    private static final List<String> SEARCH_FIELDS = List.of("name");

    @Override
    public RestResponse<ListResponse<GetListProductCategoryResponse>> getListProductCategory(
            Integer page, Integer size, Boolean isSortAscending,
            String columnName, String search, Boolean all, Boolean status, Long parentCategoryId
    ) {
        ProductCategorySpecification productCategorySpecification = new ProductCategorySpecification();
        Specification<ProductCategory> sortable = productCategorySpecification.sortable(isSortAscending, columnName);
        Specification<ProductCategory> searchable = productCategorySpecification.searchable(SEARCH_FIELDS, search);
        Specification<ProductCategory> filterableByStatus = productCategorySpecification.filterable(status);
        Specification<ProductCategory> filterableByParentCategoryId = productCategorySpecification.filterable(parentCategoryId);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListProductCategoryResponse> responses = productCategoryRepository
                .findAll(sortable.and(searchable).and(filterableByStatus).and(filterableByParentCategoryId), pageable)
                .map(productCategoryMapper::productCategoryToGetListProductCategoryResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneProductCategoryResponse> getOneProductCategory(Long id) {
        return productCategoryRepository.findById(id)
                .map(productCategoryMapper::productCategoryToGetOneProductCategoryResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<GetOneProductCategoryBySlugResponse> getOneProductCategoryBySlug(String slug) {
        return productCategoryRepository.findProductCategoryBySlug(slug)
                .map(productCategoryMapper::productCategoryToGetOneProductCategoryBySlugResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "slug", slug));
    }

    @Override
    public RestResponse<CreateProductCategoryResponse> createProductCategory(CreateProductCategoryRequest request) {
        if (request.parentId() != null && !productCategoryRepository.existsById(request.parentId())) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", request.parentId());
        }
        ProductCategory productCategory = productCategoryMapper.createProductCategoryRequestToProductCategory(request);
        productCategory.setImage(uploadService.uploadFile(request.image()));
        productCategoryRepository.save(productCategory);
        return RestResponse.created(
                productCategoryMapper.productCategoryToCreateProductCategoryResponse(productCategory)
        );
    }

    @Override
    public void updateProductCategory(Long id, UpdateProductCategoryRequest request) {
        if (request.parentId() != null && !productCategoryRepository.existsById(request.parentId())) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", request.parentId());
        }
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .map(c -> productCategoryMapper.updateProductCategoryFromUpdateProductCategoryRequest(c, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        if (request.image() != null) {
            productCategory.setImage(uploadService.uploadFile(request.image()));
        }
        productCategoryRepository.save(productCategory);
    }

    @Override
    public void deleteOneProductCategory(Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        productCategory.setStatus(false);
        productCategoryRepository.save(productCategory);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void deleteListProductCategory(List<Long> ids) {
        for (Long id : ids) {
            ProductCategory productCategory = productCategoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            productCategory.setStatus(false);
            productCategoryRepository.save(productCategory);
        }
    }

}
