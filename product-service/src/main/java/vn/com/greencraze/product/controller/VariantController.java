package vn.com.greencraze.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.variant.CreateVariantRequest;
import vn.com.greencraze.product.dto.request.variant.UpdateVariantRequest;
import vn.com.greencraze.product.dto.response.variant.CreateVariantResponse;
import vn.com.greencraze.product.dto.response.variant.GetListVariantResponse;
import vn.com.greencraze.product.dto.response.variant.GetOneVariantResponse;
import vn.com.greencraze.product.service.IVariantService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/variants")
@Tag(name = "variant :: Variant")
@RequiredArgsConstructor
public class VariantController {

    private final IVariantService variantService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of variants")
    public ResponseEntity<RestResponse<List<GetListVariantResponse>>> getListVariant(
            @RequestParam Long productId
    ) {
        return ResponseEntity.ok(variantService.getListVariant(productId));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a variant")
    public ResponseEntity<RestResponse<GetOneVariantResponse>> getOneVariant(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.getOneVariant(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a variant")
    public ResponseEntity<RestResponse<CreateVariantResponse>> createVariant(
            @RequestBody @Valid CreateVariantRequest request
    ) {
        RestResponse<CreateVariantResponse> response = variantService.creatVariant(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a variant")
    public ResponseEntity<Void> updateVariant(
            @PathVariable Long id, @RequestBody @Valid UpdateVariantRequest request
    ) {
        variantService.updateVariant(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a variant")
    public ResponseEntity<Void> deleteOneUnit(@PathVariable Long id) {
        variantService.deleteOneVariant(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of variants")
    public ResponseEntity<Void> deleteListUnit(@RequestParam List<Long> ids) {
        variantService.deleteListVariant(ids);
        return ResponseEntity.noContent().build();
    }

}
