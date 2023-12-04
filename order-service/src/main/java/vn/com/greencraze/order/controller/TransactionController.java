package vn.com.greencraze.order.controller;

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
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.dto.response.transaction.GetListTransactionResponse;
import vn.com.greencraze.order.dto.response.transaction.GetOneTransactionResponse;
import vn.com.greencraze.order.service.ITransactionService;

@RestController
@RequestMapping("/transactions")
@Tag(name = "transaction :: Transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of transactions")
    public ResponseEntity<RestResponse<ListResponse<GetListTransactionResponse>>> getListTransaction(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(transactionService.getListTransaction(
                page, size, isSortAscending, columnName, search, all));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/top5-transaction-latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a top 5 latest of transactions")
    public ResponseEntity<RestResponse<ListResponse<GetListTransactionResponse>>> getTop5TransactionLatest() {
        return ResponseEntity.ok(transactionService.getTop5TransactionLatest());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a transaction")
    public ResponseEntity<RestResponse<GetOneTransactionResponse>> getOneTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getOneTransaction(id));
    }

}
