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
import vn.com.greencraze.order.dto.request.orderCancelReason.CreateOrderCancelReasonRequest;
import vn.com.greencraze.order.dto.request.orderCancelReason.UpdateOrderCancelReasonRequest;
import vn.com.greencraze.order.dto.response.orderCancelReason.CreateOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetListOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetOneOrderCancelReasonResponse;
import vn.com.greencraze.order.service.IOrderCancelReasonService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/order-cancel-reasons")
@Tag(name = "orderCancelReason :: OrderCancelReason")
@RequiredArgsConstructor
public class OrderCancelReasonController {

    private final IOrderCancelReasonService orderCancelReasonService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of orderCancelReasons")
    public ResponseEntity<RestResponse<ListResponse<GetListOrderCancelReasonResponse>>> getListOrderCancelReason(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) boolean status
    ) {
        return ResponseEntity.ok(orderCancelReasonService.getListOrderCancelReason(
                page, size, isSortAscending, columnName, search, all, status));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get an orderCancelReason")
    public ResponseEntity<RestResponse<GetOneOrderCancelReasonResponse>> getOneOrderCancelReason(
            @PathVariable Long id) {
        return ResponseEntity.ok(orderCancelReasonService.getOneOrderCancelReason(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an orderCancelReason")
    public ResponseEntity<RestResponse<CreateOrderCancelReasonResponse>> createOrderCancelReason(
            @Valid CreateOrderCancelReasonRequest request
    ) {
        RestResponse<CreateOrderCancelReasonResponse> response = orderCancelReasonService
                .createOrderCancelReason(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update an orderCancelReason")
    public ResponseEntity<Void> updateOrderCancelReason(
            @PathVariable Long id, @Valid UpdateOrderCancelReasonRequest request
    ) {
        orderCancelReasonService.updateOrderCancelReason(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an orderCancelReason")
    public ResponseEntity<Void> deleteOneOrderCancelReason(@PathVariable Long id) {
        orderCancelReasonService.deleteOneOrderCancelReason(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of orderCancelReasons")
    public ResponseEntity<Void> deleteListOrderCancelReason(@RequestParam List<Long> ids) {
        orderCancelReasonService.deleteListOrderCancelReason(ids);
        return ResponseEntity.noContent().build();
    }

}
