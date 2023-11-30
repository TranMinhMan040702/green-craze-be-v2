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
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.dto.request.user.CreateStaffRequest;
import vn.com.greencraze.user.dto.request.user.UpdateStaffRequest;
import vn.com.greencraze.user.dto.response.user.CreateStaffResponse;
import vn.com.greencraze.user.dto.response.user.GetListStaffResponse;
import vn.com.greencraze.user.dto.response.user.GetOneStaffResponse;
import vn.com.greencraze.user.service.IUserProfileService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/staffs")
@Tag(name = "staff :: Staff")
@RequiredArgsConstructor
public class StaffController {

    private final IUserProfileService userProfileService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of staffs")
    public ResponseEntity<RestResponse<ListResponse<GetListStaffResponse>>> getListStaff(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(userProfileService.getListStaff(
                page, size, isSortAscending, columnName, search, all)
        );
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a staff")
    public ResponseEntity<RestResponse<GetOneStaffResponse>> getOneStaff(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.getOneStaff(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create staff")
    public ResponseEntity<RestResponse<CreateStaffResponse>> createStaff(
            @RequestBody @Valid CreateStaffRequest request) {
        RestResponse<CreateStaffResponse> response = userProfileService.createStaff(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update staff")
    public ResponseEntity<Void> updateStaff(@PathVariable Long id, @RequestBody @Valid UpdateStaffRequest request) {
        userProfileService.updateStaff(id, request);
        return ResponseEntity.notFound().build();
    }

    // TODO: Giữ lại toggle
    @DeleteMapping("/disable/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Disable staff")
    public ResponseEntity<Void> disableStaff(@PathVariable Long id) {
        userProfileService.disableStaff(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Disable a list staff")
    public ResponseEntity<Void> disableListStaff(@RequestParam List<Long> ids) {
        userProfileService.disableListStaff(ids);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/enable/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Enable staff")
    public ResponseEntity<Void> enableStaff(@PathVariable Long id) {
        userProfileService.enableStaff(id);
        return ResponseEntity.noContent().build();
    }

}
