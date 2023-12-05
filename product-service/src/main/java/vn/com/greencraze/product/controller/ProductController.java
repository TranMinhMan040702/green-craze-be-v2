package vn.com.greencraze.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.image.CreateProductImageRequest;
import vn.com.greencraze.product.dto.request.image.UpdateProductImageRequest;
import vn.com.greencraze.product.dto.request.product.CreateProductRequest;
import vn.com.greencraze.product.dto.request.product.ExportProductRequest;
import vn.com.greencraze.product.dto.request.product.FilterProductRequest;
import vn.com.greencraze.product.dto.request.product.ImportProductRequest;
import vn.com.greencraze.product.dto.request.product.UpdateListProductQuantityRequest;
import vn.com.greencraze.product.dto.request.product.UpdateListProductReviewRequest;
import vn.com.greencraze.product.dto.request.product.UpdateOneProductReviewRequest;
import vn.com.greencraze.product.dto.request.product.UpdateProductRequest;
import vn.com.greencraze.product.dto.response.image.CreateProductImageResponse;
import vn.com.greencraze.product.dto.response.image.GetListProductImageResponse;
import vn.com.greencraze.product.dto.response.image.GetOneProductImageResponse;
import vn.com.greencraze.product.dto.response.product.CreateProductResponse;
import vn.com.greencraze.product.dto.response.product.GetListFilteringProductResponse;
import vn.com.greencraze.product.dto.response.product.GetListProductResponse;
import vn.com.greencraze.product.dto.response.product.GetListSearchingProductResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductBySlugResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductResponse;
import vn.com.greencraze.product.service.IProductImageService;
import vn.com.greencraze.product.service.IProductService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "product :: Product")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private final IProductImageService productImageService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of products")
    public ResponseEntity<RestResponse<ListResponse<GetListProductResponse>>> getListProduct(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) boolean status,
            @RequestParam(required = false) String categorySlug
    ) {
        return ResponseEntity.ok(productService.getListProduct(
                page, size, isSortAscending, columnName, search, all, status, categorySlug));
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of searching product")
    public ResponseEntity<RestResponse<ListResponse<GetListSearchingProductResponse>>> getListSearchingProduct(
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(productService.getListSearchingProduct(search));
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of filtering products")
    public ResponseEntity<RestResponse<ListResponse<GetListFilteringProductResponse>>> getListFilteringProduct(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) boolean status,
            @Valid FilterProductRequest filter

    ) {
        return ResponseEntity.ok(productService.getListFilteringProduct(
                page, size, isSortAscending, columnName, search, all, status, filter));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a product")
    public ResponseEntity<RestResponse<GetOneProductResponse>> getOneProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getOneProduct(id));
    }

    @GetMapping(value = "/detail/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a product by slug")
    public ResponseEntity<RestResponse<GetOneProductBySlugResponse>> getOneProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.getOneProductBySlug(slug));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a product")
    public ResponseEntity<RestResponse<CreateProductResponse>> createProduct(
            @Valid CreateProductRequest request
    ) throws Exception {
        RestResponse<CreateProductResponse> response = productService.createProduct(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a product")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @Valid UpdateProductRequest request) {
        productService.updateProduct(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteOneProduct(@PathVariable Long id) {
        productService.deleteOneProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of products")
    public ResponseEntity<Void> deleteListProduct(@RequestParam List<Long> ids) {
        productService.deleteListProduct(ids);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of product images")
    public ResponseEntity<RestResponse<List<GetListProductImageResponse>>> getListProductImage(
            @RequestParam Long productId
    ) {
        return ResponseEntity.ok(productImageService.getListProductImage(productId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/images/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a product image")
    public ResponseEntity<RestResponse<GetOneProductImageResponse>> getOneProductImage(@PathVariable Long id) {
        return ResponseEntity.ok(productImageService.getOneProductImage(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(
            value = "/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a product image")
    public ResponseEntity<RestResponse<CreateProductImageResponse>> createProductImage(
            @Valid CreateProductImageRequest request
    ) {
        RestResponse<CreateProductImageResponse> response = productImageService.createProductImage(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/images/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a product image")
    public ResponseEntity<Void> updateProductImage(
            @PathVariable Long id, @Valid UpdateProductImageRequest request
    ) {
        productImageService.updateProductImage(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/images/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Set default a product image")
    public ResponseEntity<Void> setDefaultProductImage(
            @PathVariable Long id, @RequestParam Long productId
    ) {
        productImageService.setDefaultProductImage(id, productId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/images/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a product image")
    public ResponseEntity<Void> deleteOneProductImage(@PathVariable Long id) {
        productImageService.deleteOneProductImage(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of product images")
    public ResponseEntity<Void> deleteListProductImage(@RequestParam List<Long> ids) {
        productImageService.deleteListProductImage(ids);
        return ResponseEntity.noContent().build();
    }

    // TODO: Xem lại luồng export product
    // call from another service
    @GetMapping(value = "/{id}/service", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a product from other service")
    public ResponseEntity<RestResponse<GetOneProductResponse>> getOneProductFromOtherService(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getOneProduct(id));
    }

    @PutMapping(value = "/update-quantity")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a product quantity")
    public ResponseEntity<Void> updateProductQuantity(@Valid UpdateListProductQuantityRequest request) {
        productService.updateProductQuantity(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/import", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update quantity and actual inventory a product after import")
    public ResponseEntity<Void> importProduct(@RequestBody @Valid ImportProductRequest request) {
        productService.importProduct(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/export", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update quantity and actual inventory a product after export")
    public ResponseEntity<Void> exportProduct(@RequestBody @Valid ExportProductRequest request) {
        productService.exportProduct(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}/update-review")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a product review")
    public ResponseEntity<Void> updateOneProductReview(@PathVariable Long id, @Valid UpdateOneProductReviewRequest request) {
        productService.updateOneProductReview(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update-list-review")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update list product review")
    public ResponseEntity<Void> updateListProductReview(@Valid UpdateListProductReviewRequest request) {
        productService.updateListProductReview(request);
        return ResponseEntity.noContent().build();
    }

}
