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
import vn.com.greencraze.commons.domain.aggreate.CreateOrderAggregate;
import vn.com.greencraze.commons.exception.InvalidRequestException;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.dto.request.product.CreateProductRequest;
import vn.com.greencraze.product.dto.request.product.ExportProductRequest;
import vn.com.greencraze.product.dto.request.product.FilterProductRequest;
import vn.com.greencraze.product.dto.request.product.ImportProductRequest;
import vn.com.greencraze.product.dto.request.product.UpdateListProductQuantityRequest;
import vn.com.greencraze.product.dto.request.product.UpdateListProductReviewRequest;
import vn.com.greencraze.product.dto.request.product.UpdateOneProductReviewRequest;
import vn.com.greencraze.product.dto.request.product.UpdateProductRequest;
import vn.com.greencraze.product.dto.response.product.CreateProductResponse;
import vn.com.greencraze.product.dto.response.product.GetListFilteringProductResponse;
import vn.com.greencraze.product.dto.response.product.GetListProductResponse;
import vn.com.greencraze.product.dto.response.product.GetListSearchingProductResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductBySlugResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductResponse;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.ProductImage;
import vn.com.greencraze.product.entity.Variant;
import vn.com.greencraze.product.enumeration.ProductStatus;
import vn.com.greencraze.product.exception.ReduceStockException;
import vn.com.greencraze.product.mapper.ProductMapper;
import vn.com.greencraze.product.repository.BrandRepository;
import vn.com.greencraze.product.repository.ProductCategoryRepository;
import vn.com.greencraze.product.repository.ProductRepository;
import vn.com.greencraze.product.repository.UnitRepository;
import vn.com.greencraze.product.repository.VariantRepository;
import vn.com.greencraze.product.repository.specification.ProductSpecification;
import vn.com.greencraze.product.service.IProductService;
import vn.com.greencraze.product.service.IUploadService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final UnitRepository unitRepository;
    private final VariantRepository variantRepository;

    private final ProductSpecification productSpecification;

    private final ProductMapper productMapper;

    private final IUploadService uploadService;

    private final ObjectMapper objectMapper;

    private static final String RESOURCE_NAME = "Product";
    private static final List<String> SEARCH_FIELDS = List.of("name");

    @Override
    public RestResponse<ListResponse<GetListProductResponse>> getListProduct(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, Boolean status, String categorySlug
    ) {
        Specification<Product> sortable = productSpecification.sortable(isSortAscending, columnName);
        Specification<Product> searchable = productSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Product> filterableByCategorySlug = productSpecification.filterable(categorySlug);
        Specification<Product> filterableByStatus = productSpecification.filterable(status);
        Specification<Product> activeCategory = productSpecification.isActiveCategory(true);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListProductResponse> responses = productRepository
                .findAll(sortable.and(searchable).and(filterableByCategorySlug).and(filterableByStatus).and(activeCategory),
                        pageable)
                .map(productMapper::productToGetListProductResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    // TODO: getListProductForAdmin
    @Override
    public RestResponse<ListResponse<GetListProductResponse>> getListProductForAdmin(
            Integer page, Integer size, Boolean isSortAscending,
            String columnName, String search, Boolean all, Boolean status
    ) {
        Specification<Product> sortable = productSpecification.sortable(isSortAscending, columnName);
        Specification<Product> searchable = productSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Product> filterableByStatus = productSpecification.filterable(status);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListProductResponse> responses = productRepository
                .findAll(sortable.and(searchable).and(filterableByStatus), pageable)
                .map(productMapper::productToGetListProductResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<ListResponse<GetListSearchingProductResponse>> getListSearchingProduct(String search) {
        Specification<Product> searchable = productSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Product> filterableByStatus = productSpecification.filterable(true);
        Specification<Product> activeCategory = productSpecification.isActiveCategory(true);

        Pageable pageable = PageRequest.of(0, 5);
        Page<GetListSearchingProductResponse> responses = productRepository
                .findAll(searchable.and(filterableByStatus).and(activeCategory), pageable)
                .map(productMapper::productToGetListSearchingProductResponse);
        return RestResponse.created(ListResponse.of(responses));
    }

    @Override
    public RestResponse<ListResponse<GetListFilteringProductResponse>> getListFilteringProduct(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, Boolean status, FilterProductRequest filter
    ) {
        Specification<Product> sortable = productSpecification.sortable(isSortAscending, columnName);
        Specification<Product> searchable = productSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Product> filterableByStatus = productSpecification.filterable(status);
        Specification<Product> filterable = productSpecification.filterable(filter);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListFilteringProductResponse> responses = productRepository
                .findAll(Specification.where(searchable).and(filterableByStatus).and(filterable).and(sortable), pageable)
                .map(productMapper::productToGetListFilteringProductResponse);
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
        product.setStatus(ProductStatus.ACTIVE);

        request.productImages().forEach(
                image -> product.getImages().add(ProductImage.builder()
                        .image(uploadService.uploadFile(image))
                        .size((double) image.getSize())
                        .contentType(image.getContentType())
                        .product(product)
                        .build())
        );
        product.getImages().get(0).setIsDefault(true);
        Set<Variant> variants = new HashSet<>();
        for (String v : request.variants()) {
            Variant variant = objectMapper.readValue(v, Variant.class);
            variant.setProduct(product);
            variants.add(variant);
        }
        product.setVariants(variants);
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
            product.setActualInventory(product.getActualInventory() - item.quantity());
            product.setSold(product.getSold() + item.quantity());
            productRepository.save(product);
        }
    }

    @Override
    public void importProduct(ImportProductRequest request) {
        Product product = productRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", request.id()));
        product.setQuantity(product.getQuantity() + request.quantity());
        product.setActualInventory(request.actualInventory());
        productRepository.save(product);
    }

    @Override
    public void exportProduct(ExportProductRequest request) {
        Product product = productRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", request.id()));
        if (product.getQuantity() < request.quantity() || product.getActualInventory() < request.quantity()) {
            throw new InvalidRequestException("Product quantity is not enough");
        }
        product.setQuantity(product.getQuantity() - request.quantity());
        product.setActualInventory(product.getActualInventory() - request.quantity());
        productRepository.save(product);
    }

    @Override
    public void updateOneProductReview(Long id, UpdateOneProductReviewRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        product.setRating(request.rating());

        productRepository.save(product);
    }

    @Override
    public void updateListProductReview(UpdateListProductReviewRequest request) {
        for (UpdateListProductReviewRequest.UpdateOneProductReview item : request.productReviews()) {
            updateOneProductReview(item.productId(), new UpdateOneProductReviewRequest(item.rating()));
        }
    }

    @Override
    public Map<Long, BigDecimal> getListProductCost(Set<Long> ids) {
        return ids.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id))
                                .getCost()));
    }

    @Override
    public Map<String, List<Long>> getListProductWithVariant() {
        Map<String, List<Long>> response = new HashMap<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            response.put(product.getName(), product.getVariants().stream().map(Variant::getId).toList());
        }
        return response;
    }

    @Override
    public RestResponse<GetOneProductResponse> getOneProductByVariant(Long variantId) {
        Variant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "variantId", variantId));

        return productRepository.findById(variant.getProduct().getId())
                .map(productMapper::productToGetOneProductResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "productId", variant.getProduct().getId()));
    }

    @Override
    public void reduceStock(CreateOrderAggregate aggregate) {
        List<Product> productsToUpdate = new ArrayList<>();
        HashMap<Long, Long> productActualQuantity = new HashMap<>();
        for (CreateOrderAggregate.OrderItemAggregate oi : aggregate.items()) {
            Variant variant = variantRepository.findById(oi.variantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "id", oi.variantId()));

            Product product = variant.getProduct();

            if (!productActualQuantity.containsKey(product.getId())) {
                productActualQuantity.put(product.getId(), product.getActualInventory());
            }

            long quantityCurrent = (long) variant.getQuantity() * oi.quantity();
            if (quantityCurrent > productActualQuantity.get(product.getId())) {
                throw new ReduceStockException("Unexpected quantity, it must be less than or equal to product in inventory");
            }
            productActualQuantity.put(product.getId(), productActualQuantity.get(product.getId()) - quantityCurrent);

            product.setQuantity(product.getQuantity() - quantityCurrent);
            product.setActualInventory(product.getActualInventory() - quantityCurrent);
            product.setSold(product.getSold() + quantityCurrent);
            productsToUpdate.add(product);
        }
        productRepository.saveAll(productsToUpdate);
    }

    @Override
    public void revertStock(CreateOrderAggregate aggregate) {
        List<Product> productsToUpdate = new ArrayList<>();
        for (CreateOrderAggregate.OrderItemAggregate oi : aggregate.items()) {
            Variant variant = variantRepository.findById(oi.variantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "id", oi.variantId()));

            Product product = variant.getProduct();

            long quantityCurrent = (long) variant.getQuantity() * oi.quantity();

            product.setQuantity(product.getQuantity() + quantityCurrent);
            product.setActualInventory(product.getActualInventory() + quantityCurrent);
            product.setSold(product.getSold() - quantityCurrent);
            productsToUpdate.add(product);
        }
        productRepository.saveAll(productsToUpdate);
    }

}
