package vn.com.greencraze.meta.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.meta.dto.response.GetSaleLatestResponse;
import vn.com.greencraze.meta.dto.response.GetTop5OrderLatestResponse;
import vn.com.greencraze.meta.dto.response.GetTop5ReviewLatest;
import vn.com.greencraze.meta.dto.response.GetTop5TransactionLatestResponse;
import vn.com.greencraze.meta.dto.response.StatisticOrderStatusResponse;
import vn.com.greencraze.meta.dto.response.StatisticRevenueExpenseResponse;
import vn.com.greencraze.meta.dto.response.StatisticReviewResponse;
import vn.com.greencraze.meta.dto.response.StatisticTopSellingProductResponse;
import vn.com.greencraze.meta.dto.response.StatisticTopSellingProductYearResponse;
import vn.com.greencraze.meta.dto.response.StatisticTotalResponse;
import vn.com.greencraze.meta.service.IStatisticService;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@Tag(name = "statistic :: Statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final IStatisticService statisticService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/total", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Statistic total revenue, expense, user order")
    public ResponseEntity<RestResponse<StatisticTotalResponse>> statisticTotal() {
        return ResponseEntity.ok(statisticService.statisticTotal());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/revenue-expense", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Statistic revenue and expense")
    public ResponseEntity<RestResponse<List<StatisticRevenueExpenseResponse>>> statisticRevenueExpense(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(statisticService.statisticRevenueExpense(year));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/top-selling-product", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Statistic top selling product")
    public ResponseEntity<RestResponse<List<StatisticTopSellingProductResponse>>> statisticTopSellingProduct(
            @RequestParam Instant startDate, @RequestParam Instant endDate
    ) {
        return ResponseEntity.ok(statisticService.statisticTopSellingProduct(startDate, endDate));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/top-selling-product-year", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Statistic top selling product year")
    public ResponseEntity<RestResponse<List<StatisticTopSellingProductYearResponse>>> statisticTopSellingProductYear(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(statisticService.statisticTopSellingProductYear(year));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/order-status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Statistic order status")
    public ResponseEntity<RestResponse<List<StatisticOrderStatusResponse>>> statisticOrderStatus(
            @RequestParam Instant startDate, @RequestParam Instant endDate
    ) {
        return ResponseEntity.ok(statisticService.statisticOrderStatus(startDate, endDate));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/rating", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Statistic review")
    public ResponseEntity<RestResponse<List<StatisticReviewResponse>>> statisticReview(
            @RequestParam Instant startDate, @RequestParam Instant endDate
    ) {
        return ResponseEntity.ok(statisticService.statisticReview(startDate, endDate));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/top5-order-latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get top 5 order latest")
    public ResponseEntity<RestResponse<List<GetTop5OrderLatestResponse>>> getTop5OrderLatest() {
        return ResponseEntity.ok(statisticService.getTop5OrderLatest());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/top5-transaction-latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get top 5 transaction latest")
    public ResponseEntity<RestResponse<List<GetTop5TransactionLatestResponse>>> getTop5TransactionLatest() {
        return ResponseEntity.ok(statisticService.getTop5TransactionLatest());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/top5-review-latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get top 5 transaction latest")
    public ResponseEntity<RestResponse<List<GetTop5ReviewLatest>>> getTop5ReviewLatest() {
        return ResponseEntity.ok(statisticService.getTop5ReviewLatest());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/sale-latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get sale latest")
    public ResponseEntity<RestResponse<GetSaleLatestResponse>> getSaleLatest() {
        return ResponseEntity.ok(statisticService.getSaleLatest());
    }

}
