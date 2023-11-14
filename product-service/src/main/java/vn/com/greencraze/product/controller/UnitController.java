package vn.com.greencraze.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.CreateUnitRequest;
import vn.com.greencraze.product.dto.request.GetListUnitRequest;
import vn.com.greencraze.product.dto.response.CreateUnitResponse;
import vn.com.greencraze.product.dto.response.GetListUnitResponse;
import vn.com.greencraze.product.service.IUnitService;

import java.net.URI;

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
            @RequestParam(required = false) GetListUnitRequest request
    ) {
        return ResponseEntity.ok(unitService.getListUnit(request));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a unit")
    public ResponseEntity<RestResponse<CreateUnitResponse>> createUnit(@RequestBody CreateUnitRequest request) {
        RestResponse<CreateUnitResponse> response = unitService.createUnit(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

}
