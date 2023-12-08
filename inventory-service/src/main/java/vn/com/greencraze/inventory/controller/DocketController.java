package vn.com.greencraze.inventory.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.inventory.dto.request.CreateDocketRequest;
import vn.com.greencraze.inventory.dto.request.CreateDocketWithTypeExportRequest;
import vn.com.greencraze.inventory.dto.request.CreateDocketWithTypeImportRequest;
import vn.com.greencraze.inventory.dto.response.GetListDocketByProductResponse;
import vn.com.greencraze.inventory.service.IDocketService;

@RestController
@RequestMapping("/dockets")
@Tag(name = "docket :: Docket")
@RequiredArgsConstructor
public class DocketController {

    private final IDocketService docketService;

    @PostMapping(value = "/import", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Create docket with type import")
    public ResponseEntity<Void> createDocketWithTypeImport(
            @RequestBody @Valid CreateDocketWithTypeImportRequest request) {
        docketService.createDocketWithTypeImport(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/export", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Create docket with type export")
    public ResponseEntity<Void> createDocketWithTypeExport(
            @RequestBody @Valid CreateDocketWithTypeExportRequest request) {
        docketService.createDocketWithTypeExport(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Get a list docket by product Id")
    public ResponseEntity<RestResponse<GetListDocketByProductResponse>> getListDocketByProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(docketService.getListDocketByProduct(productId));
    }

    //call from another service

    @PostMapping(value = "/internal/create-docket", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Create docket")
    public ResponseEntity<Void> createDocket(
            @RequestBody @Valid CreateDocketRequest request) {
        docketService.createDocket(request);
        return ResponseEntity.noContent().build();
    }

}
