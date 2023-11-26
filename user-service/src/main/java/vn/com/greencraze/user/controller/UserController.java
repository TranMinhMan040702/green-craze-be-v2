package vn.com.greencraze.user.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.dto.request.user.CreateUserRequest;
import vn.com.greencraze.user.dto.response.user.CreateUserResponse;
import vn.com.greencraze.user.dto.response.user.GetListUserResponse;
import vn.com.greencraze.user.dto.response.user.GetOneUserResponse;
import vn.com.greencraze.user.service.IUserProfileService;

import java.net.URI;

@RestController
@RequestMapping("/users")
@Tag(name = "user :: User")
@RequiredArgsConstructor
public class UserController {

    private final IUserProfileService userProfileService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of users")
    public ResponseEntity<RestResponse<ListResponse<GetListUserResponse>>> getListUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(userProfileService.getListUser(
                page, size, isSortAscending, columnName, search, all)
        );
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user")
    public ResponseEntity<RestResponse<GetOneUserResponse>> getOneUser(@PathVariable String id) {
        return ResponseEntity.ok(userProfileService.getOneUser(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create user")
    public ResponseEntity<RestResponse<CreateUserResponse>> createUser(@RequestBody @Valid CreateUserRequest request) {
        RestResponse<CreateUserResponse> response = userProfileService.createUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

}
