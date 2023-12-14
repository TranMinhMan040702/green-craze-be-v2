package vn.com.greencraze.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.com.greencraze.auth.dto.response.role.GetListRoleResponse;
import vn.com.greencraze.auth.dto.response.role.GetOneRoleResponse;
import vn.com.greencraze.auth.service.IRoleService;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;

@RestController
@RequestMapping("/roles")
@Tag(name = "role :: Role")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of roles")
    public ResponseEntity<RestResponse<ListResponse<GetListRoleResponse>>> getListRole(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) boolean status
    ) {
        return ResponseEntity.ok(roleService.getListRole(page, size, isSortAscending, columnName, search, all, status));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a role")
    public ResponseEntity<RestResponse<GetOneRoleResponse>> getOneRole(@PathVariable String id) {
        return ResponseEntity.ok(roleService.getOneRole(id));
    }

}
