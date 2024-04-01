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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.annotation.InternalApi;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.enumeration.Microservice;
import vn.com.greencraze.user.dto.request.user.CreateUserRequest;
import vn.com.greencraze.user.dto.request.user.UpdateUserRequest;
import vn.com.greencraze.user.dto.response.user.CreateUserResponse;
import vn.com.greencraze.user.dto.response.user.GetListUserResponse;
import vn.com.greencraze.user.dto.response.user.GetMeResponse;
import vn.com.greencraze.user.dto.response.user.GetOneUserResponse;
import vn.com.greencraze.user.service.IUserProfileService;

import java.net.URI;
import java.util.List;

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
                page, size, isSortAscending, columnName, search, all));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user")
    public ResponseEntity<RestResponse<GetOneUserResponse>> getOneUser(@PathVariable String id) {
        return ResponseEntity.ok(userProfileService.getOneUser(id));
    }

    @GetMapping(value = "/profile/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user")
    public ResponseEntity<RestResponse<GetMeResponse>> getMe() {
        return ResponseEntity.ok(userProfileService.getMe());
    }

    @InternalApi(Microservice.AUTH)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create user")
    public ResponseEntity<RestResponse<CreateUserResponse>> createUser(@RequestBody @Valid CreateUserRequest request) {
        RestResponse<CreateUserResponse> response = userProfileService.createUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update user")
    public ResponseEntity<Void> updateUser(@Valid UpdateUserRequest request) {
        userProfileService.updateUser(request);
        return ResponseEntity.noContent().build();
    }

    // TODO: Giữ lại toggle
    @DeleteMapping("/disable/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Disable user")
    public ResponseEntity<Void> disableUser(@PathVariable String id) {
        userProfileService.disableUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Disable a list user")
    public ResponseEntity<Void> disableListUser(@RequestParam List<String> ids) {
        userProfileService.disableListUser(ids);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Toggle user status")
    public ResponseEntity<Void> toggleUserStatus(@PathVariable String id) {
        userProfileService.toggleUserStatus(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/enable/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Enable user")
    public ResponseEntity<Void> enableUser(@PathVariable String id) {
        userProfileService.enableUser(id);
        return ResponseEntity.noContent().build();
    }

    @InternalApi(Microservice.ORDER)
    @GetMapping(value = "/other/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user")
    public ResponseEntity<RestResponse<GetOneUserResponse>> getOneUserFromOtherService(@PathVariable String id) {
        return ResponseEntity.ok(userProfileService.getOneUser(id));
    }

    @InternalApi(Microservice.META)
    @GetMapping("/total")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get total user")
    public ResponseEntity<Long> getTotalUser() {
        return ResponseEntity.ok(userProfileService.getTotalUser());
    }

    @InternalApi(Microservice.INFRASTRUCTURE)
    @GetMapping("/get-all-user-id")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get total user")
    public ResponseEntity<List<String>> getAllUserId() {
        return ResponseEntity.ok(userProfileService.getAllUserId());
    }

    @InternalApi(Microservice.INFRASTRUCTURE)
    @GetMapping("/get-username/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get username of user")
    public ResponseEntity<String> getUsername(@PathVariable String userId) {
        return ResponseEntity.ok(userProfileService.getUsername(userId));
    }

}
