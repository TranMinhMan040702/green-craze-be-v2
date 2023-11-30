package vn.com.greencraze.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.dto.request.userFollowProduct.FollowProductRequest;
import vn.com.greencraze.user.dto.response.userFollowProduct.CreateUserFollowProductResponse;
import vn.com.greencraze.user.dto.response.userFollowProduct.GetListUserFollowProductResponse;
import vn.com.greencraze.user.service.IUserFollowProductService;

import java.net.URI;

@RestController
@RequestMapping("/user-follow-products")
@Tag(name = "userFollowProduct :: UserFollowProducts")
@RequiredArgsConstructor
public class UserFollowProductController {

    private final IUserFollowProductService userFollowProductService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of reviews")
    public ResponseEntity<RestResponse<ListResponse<GetListUserFollowProductResponse>>> getListFollowingProduct(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(userFollowProductService.getListFollowingProduct(page, size, isSortAscending,
                columnName, search, all));
    }

    @PostMapping(value = "/like", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a userFollowProduct")
    public ResponseEntity<RestResponse<CreateUserFollowProductResponse>> followProduct(
            @Valid FollowProductRequest request
    ) {
        RestResponse<CreateUserFollowProductResponse> response = userFollowProductService.followProduct(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping(value = "/unlike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Create a userFollowProduct")
    public ResponseEntity<Void> unfollowProduct(@Valid FollowProductRequest request) {
        userFollowProductService.unfollowProduct(request);
        return ResponseEntity.noContent().build();
    }

}
