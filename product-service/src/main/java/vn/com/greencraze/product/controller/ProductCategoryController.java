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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.category.CreateProductCategoryRequest;
import vn.com.greencraze.product.dto.request.category.UpdateProductCategoryRequest;
import vn.com.greencraze.product.dto.response.category.CreateProductCategoryResponse;
import vn.com.greencraze.product.dto.response.category.GetListProductCategoryResponse;
import vn.com.greencraze.product.dto.response.category.GetOneProductCategoryBySlugResponse;
import vn.com.greencraze.product.dto.response.category.GetOneProductCategoryResponse;
import vn.com.greencraze.product.service.IProductCategoryService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/product-categories")
@Tag(name = "productCategory :: ProductCategory")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final IProductCategoryService productCategoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of product categories")
    public ResponseEntity<RestResponse<ListResponse<GetListProductCategoryResponse>>> getListProductCategory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) boolean status
    ) {
        return ResponseEntity.ok(productCategoryService.getListProductCategory(
                page, size, isSortAscending, columnName, search, all, status));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a product category")
    public ResponseEntity<RestResponse<GetOneProductCategoryResponse>> getOneProductCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productCategoryService.getOneProductCategory(id));
    }

    @GetMapping(value = "/slug/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a product category by slug")
    public ResponseEntity<RestResponse<GetOneProductCategoryBySlugResponse>> getOneProductCategoryBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(productCategoryService.getOneProductCategoryBySlug(slug));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a product category")
    public ResponseEntity<RestResponse<CreateProductCategoryResponse>> createProductCategory(
            @Valid CreateProductCategoryRequest request
    ) {
        RestResponse<CreateProductCategoryResponse> response = productCategoryService.createProductCategory(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a product category")
    public ResponseEntity<Void> updateProductCategory(
            @PathVariable Long id, @Valid UpdateProductCategoryRequest request
    ) {
        productCategoryService.updateProductCategory(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a product category")
    public ResponseEntity<Void> deleteOneProductCategory(@PathVariable Long id) {
        productCategoryService.deleteOneProductCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of product category")
    public ResponseEntity<Void> deleteListProductCategory(@RequestParam List<Long> ids) {
        productCategoryService.deleteListProductCategory(ids);
        return ResponseEntity.noContent().build();
    }

}
