package vn.com.greencraze.user.controller;

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
import vn.com.greencraze.user.dto.request.cart.CreateCartItemRequest;
import vn.com.greencraze.user.dto.request.cart.UpdateCartItemRequest;
import vn.com.greencraze.user.dto.response.cart.CreateCartItemResponse;
import vn.com.greencraze.user.dto.response.cart.GetListCartItemResponse;
import vn.com.greencraze.user.service.ICartService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/carts")
@Tag(name = "cart :: Cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of cart items")
    public ResponseEntity<RestResponse<ListResponse<GetListCartItemResponse>>> getCartByUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(cartService.getCartByUser(page, size, isSortAscending, columnName, search, all));
    }

    @GetMapping(value = "/list-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of cart items")
    public ResponseEntity<RestResponse<List<GetListCartItemResponse>>> getCartItemByListIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(cartService.getCartItemByListId(ids));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a cart item")
    public ResponseEntity<RestResponse<CreateCartItemResponse>> createCartItem(
            @Valid CreateCartItemRequest request
    ) {
        RestResponse<CreateCartItemResponse> response = cartService.createCartItem(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a cart item")
    public ResponseEntity<Void> updateAddress(
            @PathVariable Long id, @Valid UpdateCartItemRequest request
    ) {
        cartService.updateCartItem(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a cart item")
    public ResponseEntity<Void> deleteOneAddress(@PathVariable Long id) {
        cartService.deleteOneCartItem(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of cart items")
    public ResponseEntity<Void> deleteListBrand(@RequestParam List<Long> ids) {
        cartService.deleteListCartItem(ids);

        return ResponseEntity.noContent().build();
    }

}
