package vn.com.greencraze.order.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.dto.request.delivery.CreateDeliveryRequest;
import vn.com.greencraze.order.dto.request.delivery.UpdateDeliveryRequest;
import vn.com.greencraze.order.dto.response.delivery.CreateDeliveryResponse;
import vn.com.greencraze.order.dto.response.delivery.GetListDeliveryResponse;
import vn.com.greencraze.order.dto.response.delivery.GetOneDeliveryResponse;
import vn.com.greencraze.order.service.IDeliveryService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/deliveries")
@Tag(name = "delivery :: Delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final IDeliveryService deliveryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of deliveries")
    public ResponseEntity<RestResponse<ListResponse<GetListDeliveryResponse>>> getListDelivery(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) boolean status
    ) {
        return ResponseEntity.ok(deliveryService.getListDelivery(
                page, size, isSortAscending, columnName, search, all, status));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a delivery")
    public ResponseEntity<RestResponse<GetOneDeliveryResponse>> getOneDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getOneDelivery(id));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a delivery")
    public ResponseEntity<RestResponse<CreateDeliveryResponse>> createDelivery(
            @Valid CreateDeliveryRequest request
    ) {
        RestResponse<CreateDeliveryResponse> response = deliveryService.createDelivery(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a delivery")
    public ResponseEntity<Void> updateDelivery(
            @PathVariable Long id, @Valid UpdateDeliveryRequest request
    ) {
        deliveryService.updateDelivery(id, request);
        return ResponseEntity.noContent().build();
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a delivery")
    public ResponseEntity<Void> deleteOneDelivery(@PathVariable Long id) {
        deliveryService.deleteOneDelivery(id);
        return ResponseEntity.noContent().build();
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of deliveries")
    public ResponseEntity<Void> deleteListDelivery(@RequestParam List<Long> ids) {
        deliveryService.deleteListDelivery(ids);
        return ResponseEntity.noContent().build();
    }

}
