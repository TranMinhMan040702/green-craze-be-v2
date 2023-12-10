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
import vn.com.greencraze.commons.annotation.InternalApi;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.enumeration.Microservice;
import vn.com.greencraze.product.dto.request.sale.CreateSaleRequest;
import vn.com.greencraze.product.dto.request.sale.UpdateSaleRequest;
import vn.com.greencraze.product.dto.response.sale.CreateSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetListSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetSaleLatestResponse;
import vn.com.greencraze.product.service.ISaleService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sales")
@Tag(name = "sale :: Sale")
@RequiredArgsConstructor
public class SaleController {

    private final ISaleService saleService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of sales")
    public ResponseEntity<RestResponse<ListResponse<GetListSaleResponse>>> getListSale(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(saleService.getListSale(page, size, isSortAscending, columnName, search, all));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a sale")
    public ResponseEntity<RestResponse<GetOneSaleResponse>> getOneSale(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getOneSale(id));
    }

    @InternalApi(Microservice.META)
    @GetMapping(value = "/sale-latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a sale latest")
    public ResponseEntity<GetSaleLatestResponse> getSaleLatest() {
        return ResponseEntity.ok(saleService.getSaleLatest());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a sale")
    public ResponseEntity<RestResponse<CreateSaleResponse>> createSale(@Valid CreateSaleRequest request) {
        RestResponse<CreateSaleResponse> response = saleService.createSale(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a sale")
    public ResponseEntity<RestResponse<CreateSaleResponse>> updateSale(
            @PathVariable Long id, @Valid UpdateSaleRequest request
    ) {
        saleService.updateSale(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a sale")
    public ResponseEntity<Void> deleteOneSale(@PathVariable Long id) {
        saleService.deleteOneSale(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of sales")
    public ResponseEntity<Void> deleteListSale(@RequestParam List<Long> ids) {
        saleService.deleteListSale(ids);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Apply a sale")
    public ResponseEntity<Void> applySale(@RequestParam Long id) {
        saleService.applySale(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel a sale")
    public ResponseEntity<Void> cancelSale(@RequestParam Long id) {
        saleService.cancelSale(id);
        return ResponseEntity.noContent().build();
    }

}
