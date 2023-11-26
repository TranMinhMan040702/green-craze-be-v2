package vn.com.greencraze.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import vn.com.greencraze.order.dto.request.order.CompletePaypalOrderRequest;
import vn.com.greencraze.order.dto.request.order.CreateOrderRequest;
import vn.com.greencraze.order.dto.request.order.UpdateOrderRequest;
import vn.com.greencraze.order.dto.response.order.CreateOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderResponse;
import vn.com.greencraze.order.service.IOrderService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "order :: Order")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of order")
    public ResponseEntity<RestResponse<ListResponse<GetListOrderResponse>>> getListOrder(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(orderService.getListOrder(page, size, isSortAscending, columnName, search, all, status));
    }

    @GetMapping(value = "/me/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of user order")
    public ResponseEntity<RestResponse<ListResponse<GetListOrderResponse>>> getListUserOrder(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(orderService.getListUserOrder(page, size, isSortAscending, columnName, search, all, status));
    }

    @GetMapping(value = "/top5-order-latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of user order")
    public ResponseEntity<RestResponse<List<GetListOrderResponse>>> getTop5OrderLatest() {
        return ResponseEntity.ok(orderService.getTop5OrderLatest());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get an order")
    public ResponseEntity<RestResponse<GetOneOrderResponse>> getOneOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOneOrder(id));
    }

    @GetMapping(value = "/detail/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get an order by code")
    public ResponseEntity<RestResponse<GetOneOrderResponse>> getOneOrder(@PathVariable String code) {
        return ResponseEntity.ok(orderService.getOneOrderByCode(code));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an order")
    public ResponseEntity<RestResponse<CreateOrderResponse>> createOrder(
            @Valid CreateOrderRequest request
    ) {
        RestResponse<CreateOrderResponse> response = orderService.createOrder(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a order")
    public ResponseEntity<Void> updateOrder(
            @PathVariable Long id, @Valid UpdateOrderRequest request
    ) {
        orderService.updateOrder(id, request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/paypal/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a order")
    public ResponseEntity<Void> completePaypalOrder(
            @PathVariable Long id, @Valid CompletePaypalOrderRequest request
    ) {
        orderService.completePaypalOrder(id, request);

        return ResponseEntity.noContent().build();
    }
}
