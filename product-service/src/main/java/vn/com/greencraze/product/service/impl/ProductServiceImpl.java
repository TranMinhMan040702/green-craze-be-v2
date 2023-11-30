package vn.com.greencraze.product.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import vn.com.greencraze.product.dto.request.product.CreateProductRequest;
import vn.com.greencraze.product.dto.request.product.UpdateListProductQuantityRequest;
import vn.com.greencraze.product.dto.request.product.UpdateProductRequest;
import vn.com.greencraze.product.dto.response.product.CreateProductResponse;
import vn.com.greencraze.product.dto.response.product.GetListProductResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductBySlugResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductResponse;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.ProductImage;
import vn.com.greencraze.product.entity.Variant;
import vn.com.greencraze.product.enumeration.ProductStatus;
import vn.com.greencraze.product.mapper.ProductMapper;
import vn.com.greencraze.product.repository.BrandRepository;
import vn.com.greencraze.product.repository.ProductCategoryRepository;
import vn.com.greencraze.product.repository.ProductRepository;
import vn.com.greencraze.product.repository.UnitRepository;
import vn.com.greencraze.product.repository.specification.ProductSpecification;
import vn.com.greencraze.product.service.IProductService;
import vn.com.greencraze.product.service.IUploadService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final UnitRepository unitRepository;
    private final ProductMapper productMapper;
    private final IUploadService uploadService;
    private final ObjectMapper objectMapper;

    private static final String RESOURCE_NAME = "Product";
    private static final List<String> SEARCH_FIELDS = List.of("name");

    @Override
    public RestResponse<ListResponse<GetListProductResponse>> getListProduct(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all,
            String categorySlug, BigDecimal minPrice, BigDecimal maxPrice, Long brandId
    ) {
        ProductSpecification productSpecification = new ProductSpecification();
        Specification<Product> sortable = productSpecification.sortable(isSortAscending, columnName);
        Specification<Product> searchable = productSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Product> filterable = productSpecification.filterable(
                categorySlug, minPrice, maxPrice, brandId
        );

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListProductResponse> responses = productRepository
                .findAll(sortable.and(searchable).and(filterable), pageable)
                .map(productMapper::productToGetListProductResponse);
        return RestResponse.created(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneProductResponse> getOneProduct(Long id) {
        return productRepository.findById(id)
                .map(productMapper::productToGetOneProductResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<GetOneProductBySlugResponse> getOneProductBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .map(productMapper::productToGetOneProductBySlugResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "slug", slug));
    }

    @Override
    public RestResponse<CreateProductResponse> createProduct(CreateProductRequest request) throws Exception {
        if (!productCategoryRepository.existsById(request.categoryId())) {
            throw new ResourceNotFoundException("ProductCategory", "id", request.categoryId());
        }
        if (!brandRepository.existsById(request.brandId())) {
            throw new ResourceNotFoundException("Brand", "id", request.brandId());
        }
        if (!unitRepository.existsById(request.unitId())) {
            throw new ResourceNotFoundException("Unit", "id", request.unitId());
        }

        Product product = productMapper.createProductRequestToProduct(request);

        request.productImages().forEach(
                image -> product.getImages().add(ProductImage.builder()
                        .image(uploadService.uploadFile(image))
                        .size((double) image.getSize())
                        .contentType(image.getContentType())
                        .product(product)
                        .build())
        );
        product.getImages().get(0).setIsDefault(true);

        //        for (String variant : request.variants()) {
        //            variants.add(objectMapper.readValue(variant, Variant.class));
        //        }
        // TODO: test convert JSON to Object
        String v = """
                {"name":"Lốc","sku":"HGCC-1","itemPrice":450,"quantity":100,"totalPrice":45000,"status":"ACTIVE"}
                """;
        Variant variant = objectMapper.readValue(v, Variant.class);
        variant.setProduct(product);
        product.getVariants().add(variant);

        productRepository.save(product);

        return RestResponse.created(productMapper.productToCreateProductResponse(product));
    }

    @Override
    public void updateProduct(Long id, UpdateProductRequest request) {
        if (!productCategoryRepository.existsById(request.categoryId())) {
            throw new ResourceNotFoundException("ProductCategory", "id", request.categoryId());
        }
        if (!brandRepository.existsById(request.brandId())) {
            throw new ResourceNotFoundException("Brand", "id", request.brandId());
        }
        if (!unitRepository.existsById(request.unitId())) {
            throw new ResourceNotFoundException("Unit", "id", request.unitId());
        }
        Product product = productRepository.findById(id)
                .map(p -> productMapper.updateProductFromUpdateProductRequest(p, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        productRepository.save(product);
    }


    @Override
    public void deleteOneProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void deleteListProduct(List<Long> ids) {
        for (Long id : ids) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            product.setStatus(ProductStatus.INACTIVE);
            productRepository.save(product);
        }
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void updateProductQuantity(UpdateListProductQuantityRequest request) {
        for (UpdateListProductQuantityRequest.ProductQuantityItem item : request.quantityItems()) {
            Product product = productRepository.findById(item.id())
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", item.id()));
            product.setQuantity(product.getQuantity() - item.quantity());
            product.setSold(product.getSold() + item.quantity());
            productRepository.save(product);
        }
    }

}
