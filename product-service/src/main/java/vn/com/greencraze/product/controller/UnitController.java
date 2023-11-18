package vn.com.greencraze.product.controller;

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
import vn.com.greencraze.product.dto.request.unit.CreateUnitRequest;
import vn.com.greencraze.product.dto.request.unit.UpdateUnitRequest;
import vn.com.greencraze.product.dto.response.unit.CreateUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetListUnitResponse;
import vn.com.greencraze.product.dto.response.unit.GetOneUnitResponse;
import vn.com.greencraze.product.service.IUnitService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/units")
@Tag(name = "unit :: Unit")
@RequiredArgsConstructor
public class UnitController {

    private final IUnitService unitService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of units")
    public ResponseEntity<RestResponse<ListResponse<GetListUnitResponse>>> getListUnit(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(unitService.getListUnit(page, size, isSortAscending, columnName, search, all));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a units")
    public ResponseEntity<RestResponse<GetOneUnitResponse>> getOneUnit(@PathVariable Long id) {
        return ResponseEntity.ok(unitService.getOneUnit(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a unit")
    public ResponseEntity<RestResponse<CreateUnitResponse>> createUnit(@RequestBody @Valid CreateUnitRequest request) {
        RestResponse<CreateUnitResponse> response = unitService.createUnit(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a unit")
    public ResponseEntity<Void> updateUnit(
            @PathVariable Long id, @RequestBody @Valid UpdateUnitRequest request
    ) {
        unitService.updateUnit(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a unit")
    public ResponseEntity<Void> deleteOneUnit(@PathVariable Long id) {
        unitService.deleteOneUnit(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of unit")
    public ResponseEntity<Void> deleteListUnit(@RequestParam List<Long> ids) {
        unitService.deleteListUnit(ids);
        return ResponseEntity.noContent().build();
    }

}
