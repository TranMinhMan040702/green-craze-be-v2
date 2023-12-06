package vn.com.greencraze.address.controller;

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
import vn.com.greencraze.address.dto.request.address.CreateAddressRequest;
import vn.com.greencraze.address.dto.request.address.UpdateAddressRequest;
import vn.com.greencraze.address.dto.response.address.CreateAddressResponse;
import vn.com.greencraze.address.dto.response.address.GetListAddressByUserIdResponse;
import vn.com.greencraze.address.dto.response.address.GetListAddressResponse;
import vn.com.greencraze.address.dto.response.address.GetOneAddressResponse;
import vn.com.greencraze.address.dto.response.district.GetListDistrictResponse;
import vn.com.greencraze.address.dto.response.province.GetListProvinceResponse;
import vn.com.greencraze.address.dto.response.ward.GetListWardResponse;
import vn.com.greencraze.address.service.IAddressService;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/addresses")
@Tag(name = "address :: Address")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of addresses")
    public ResponseEntity<RestResponse<ListResponse<GetListAddressResponse>>> getListAddress(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "false") boolean isSortAscending,
            @RequestParam(defaultValue = "isDefault") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) boolean status
    ) {
        return ResponseEntity.ok(addressService.getListAddress(
                page, size, isSortAscending, columnName, search, all, status
        ));
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list addresses of user")
    public ResponseEntity<RestResponse<List<GetListAddressByUserIdResponse>>> getListAddressByUserId(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(addressService.getListAddressByUserId(id));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a address")
    public ResponseEntity<RestResponse<GetOneAddressResponse>> getOneAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getOneAddress(id));
    }

    @GetMapping(value = "/default", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a default address")
    public ResponseEntity<RestResponse<GetOneAddressResponse>> getDefaultAddress() {
        return ResponseEntity.ok(addressService.getDefaultAddress());
    }

    @GetMapping(value = "/p", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of province")
    public ResponseEntity<RestResponse<List<GetListProvinceResponse>>> getListProvince() {
        return ResponseEntity.ok(addressService.getListProvince());
    }

    @GetMapping(value = "/p/{id}/d", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of district by province id")
    public ResponseEntity<RestResponse<List<GetListDistrictResponse>>> getListDistrictByProvince(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getListDistrictByProvince(id));
    }


    @GetMapping(value = "/p/d/{id}/w", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of district by province id")
    public ResponseEntity<RestResponse<List<GetListWardResponse>>> getListWardByDistrict(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getListWardByDistrict(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a address")
    public ResponseEntity<RestResponse<CreateAddressResponse>> createAddress(
            @RequestBody @Valid CreateAddressRequest request
    ) {
        RestResponse<CreateAddressResponse> response = addressService.createAddress(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a address")
    public ResponseEntity<Void> updateAddress(
            @PathVariable Long id,
            @RequestBody @Valid UpdateAddressRequest request
    ) {
        addressService.updateAddress(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/set-default/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Set default to address")
    public ResponseEntity<Void> setDefaultAddress(@PathVariable Long id) {
        addressService.setAddressDefault(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a address")
    public ResponseEntity<Void> deleteOneAddress(@PathVariable Long id) {
        addressService.deleteOneAddress(id);
        return ResponseEntity.noContent().build();
    }

    // call from another service
    @GetMapping(value = "/default/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a default address")
    public ResponseEntity<RestResponse<GetOneAddressResponse>> getDefaultAddress(@PathVariable String userId) {
        return ResponseEntity.ok(addressService.getDefaultUserAddress(userId));
    }

}
