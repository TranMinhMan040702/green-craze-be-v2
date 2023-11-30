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
import vn.com.greencraze.product.dto.request.brand.CreateBrandRequest;
import vn.com.greencraze.product.dto.request.brand.UpdateBrandRequest;
import vn.com.greencraze.product.dto.response.brand.CreateBrandResponse;
import vn.com.greencraze.product.dto.response.brand.GetListBrandResponse;
import vn.com.greencraze.product.dto.response.brand.GetOneBrandResponse;
import vn.com.greencraze.product.service.IBrandService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/brands")
@Tag(name = "brand :: Brand")
@RequiredArgsConstructor
public class BrandController {

    private final IBrandService brandService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of brands")
    public ResponseEntity<RestResponse<ListResponse<GetListBrandResponse>>> getListBrand(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(brandService.getListBrand(page, size, isSortAscending, columnName, search, all));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a brand")
    public ResponseEntity<RestResponse<GetOneBrandResponse>> getOneBrand(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getOneBrand(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a brand")
    public ResponseEntity<RestResponse<CreateBrandResponse>> createBrand(
            @Valid CreateBrandRequest request
    ) {
        RestResponse<CreateBrandResponse> response = brandService.createBrand(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a brand")
    public ResponseEntity<Void> updateBrand(
            @PathVariable Long id, @Valid UpdateBrandRequest request
    ) {
        brandService.updateBrand(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a brand")
    public ResponseEntity<Void> deleteOneBrand(@PathVariable Long id) {
        brandService.deleteOneBrand(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of brands")
    public ResponseEntity<Void> deleteListBrand(@RequestParam List<Long> ids) {
        brandService.deleteListBrand(ids);
        return ResponseEntity.noContent().build();
    }

}
