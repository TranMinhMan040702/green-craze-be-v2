package vn.com.greencraze.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.auth.dto.request.identity.ChangePasswordRequest;
import vn.com.greencraze.auth.dto.request.identity.CreateIdentityRequest;
import vn.com.greencraze.auth.dto.request.identity.DisableListIdentityRequest;
import vn.com.greencraze.auth.dto.request.identity.UpdateIdentityRequest;
import vn.com.greencraze.auth.dto.request.identity.UpdateIdentityStatusRequest;
import vn.com.greencraze.auth.dto.response.user.CreateIdentityResponse;
import vn.com.greencraze.auth.service.IIdentityService;
import vn.com.greencraze.commons.annotation.InternalApi;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.enumeration.Microservice;

import java.net.URI;

@RestController
@RequestMapping("/identities")
@Tag(name = "identity :: Identity")
@RequiredArgsConstructor
public class IdentityController {

    private final IIdentityService identityService;

    @InternalApi(Microservice.USER)
    @PutMapping(value = "/update-status", consumes = MediaType.APPLICATION_JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update status a identity")
    public ResponseEntity<Void> updateIdentityStatus(@RequestBody @Valid UpdateIdentityStatusRequest request) {
        identityService.updateIdentityStatus(request);
        return ResponseEntity.noContent().build();
    }

    @InternalApi(Microservice.USER)
    @PutMapping(value = "/disable", consumes = MediaType.APPLICATION_JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Disable a list identities")
    public ResponseEntity<Void> disableListIdentity(@RequestBody @Valid DisableListIdentityRequest request) {
        identityService.disableListIdentity(request);
        return ResponseEntity.noContent().build();
    }

    @InternalApi(Microservice.USER)
    @PostMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a identity")
    public ResponseEntity<RestResponse<CreateIdentityResponse>> createIdentity(
            @RequestBody @Valid CreateIdentityRequest request
    ) {
        RestResponse<CreateIdentityResponse> response = identityService.createIdentity(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @InternalApi(Microservice.USER)
    @PutMapping(value = "/update-identity", consumes = MediaType.APPLICATION_JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a identity")
    public ResponseEntity<Void> updateIdentity(@RequestBody @Valid UpdateIdentityRequest request) {
        identityService.updateIdentity(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Change password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        identityService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

}
