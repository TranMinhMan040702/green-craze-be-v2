package vn.com.greencraze.order.controller;

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
import vn.com.greencraze.order.dto.request.paymentMethod.CreatePaymentMethodRequest;
import vn.com.greencraze.order.dto.request.paymentMethod.UpdatePaymentMethodRequest;
import vn.com.greencraze.order.dto.response.paymentMethod.CreatePaymentMethodResponse;
import vn.com.greencraze.order.dto.response.paymentMethod.GetListPaymentMethodResponse;
import vn.com.greencraze.order.dto.response.paymentMethod.GetOnePaymentMethodResponse;
import vn.com.greencraze.order.service.IPaymentMethodService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/payment-methods")
@Tag(name = "paymentMethod :: PaymentMethod")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final IPaymentMethodService paymentMethodService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of paymentMethods")
    public ResponseEntity<RestResponse<ListResponse<GetListPaymentMethodResponse>>> getListPaymentMethod(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(paymentMethodService.getListPaymentMethod(
                page, size, isSortAscending, columnName, search, all));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a paymentMethod")
    public ResponseEntity<RestResponse<GetOnePaymentMethodResponse>> getOnePaymentMethod(@PathVariable Long id) {
        return ResponseEntity.ok(paymentMethodService.getOnePaymentMethod(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a paymentMethod")
    public ResponseEntity<RestResponse<CreatePaymentMethodResponse>> createPaymentMethod(
            @Valid CreatePaymentMethodRequest request
    ) {
        RestResponse<CreatePaymentMethodResponse> response = paymentMethodService.createPaymentMethod(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a paymentMethod")
    public ResponseEntity<Void> updatePaymentMethod(
            @PathVariable Long id, @Valid UpdatePaymentMethodRequest request
    ) {
        paymentMethodService.updatePaymentMethod(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a paymentMethod")
    public ResponseEntity<Void> deleteOnePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deleteOnePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of paymentMethods")
    public ResponseEntity<Void> deleteListPaymentMethod(@RequestParam List<Long> ids) {
        paymentMethodService.deleteListPaymentMethod(ids);
        return ResponseEntity.noContent().build();
    }

}
