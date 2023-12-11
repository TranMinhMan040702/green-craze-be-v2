package vn.com.greencraze.meta.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.meta.client.inventory.InventoryServiceClient;
import vn.com.greencraze.meta.client.order.OrderServiceClient;
import vn.com.greencraze.meta.client.product.ProductServiceClient;
import vn.com.greencraze.meta.client.user.UserServiceClient;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements IStatisticService {

    private final UserServiceClient userServiceClient;
    private final OrderServiceClient orderServiceClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final ProductServiceClient productServiceClient;

    @Override
    public RestResponse<StatisticTotalResponse> statisticTotal() {
        return RestResponse.ok(StatisticTotalResponse.builder()
                .revenue(orderServiceClient.getRevenue())
                .expense(inventoryServiceClient.getExpense())
                .user(userServiceClient.getTotalUser())
                .order(orderServiceClient.getTotalOrderWithStatusDelivered())
                .build());
    }

    @Override
    public RestResponse<List<StatisticRevenueExpenseResponse>> statisticRevenueExpense(int year) {
        List<StatisticRevenueExpenseResponse> responses = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            Instant firstDayOfMonthInstant = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant lastDayOfMonthInstant = yearMonth.atEndOfMonth().atStartOfDay(ZoneOffset.UTC).toInstant();

            BigDecimal revenue = orderServiceClient.getRevenueByCreatedAt(
                    firstDayOfMonthInstant, lastDayOfMonthInstant);

            BigDecimal expense = inventoryServiceClient.getExpenseByCreatedAt(
                    firstDayOfMonthInstant, lastDayOfMonthInstant);

            responses.add(StatisticRevenueExpenseResponse.builder()
                    .date("Tháng " + month)
                    .revenue(revenue)
                    .expense(expense)
                    .build());
        }
        return RestResponse.ok(responses);
    }

    @Override
    public RestResponse<List<StatisticTopSellingProductResponse>> statisticTopSellingProduct(
            Instant startDate, Instant endDate
    ) {
        List<StatisticTopSellingProductResponse> responses = new ArrayList<>();
        Map<String, Long> productWithQuantity = orderServiceClient.getTopSellingProduct(startDate, endDate);

        productWithQuantity.forEach((key, value) -> responses.add(StatisticTopSellingProductResponse.builder()
                .name(key)
                .value(value)
                .build()));

        return RestResponse.ok(responses.stream()
                .sorted(Comparator.comparingLong(StatisticTopSellingProductResponse::value).reversed())
                .limit(5)
                .collect(Collectors.toList()));
    }

    @Override
    public RestResponse<List<StatisticTopSellingProductYearResponse>> statisticTopSellingProductYear(int year) {
        List<StatisticTopSellingProductYearResponse> responses = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            Instant firstDayOfMonthInstant = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant lastDayOfMonthInstant = yearMonth.atEndOfMonth().atStartOfDay(ZoneOffset.UTC).toInstant();

            Map<String, Long> productWithQuantity = orderServiceClient.getTopSellingProduct(
                    firstDayOfMonthInstant, lastDayOfMonthInstant);

            Map<String, Long> topSelling = productWithQuantity.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            responses.add(StatisticTopSellingProductYearResponse.builder()
                    .date("Tháng " + month)
                    .products(topSelling)
                    .build());
        }
        return RestResponse.ok(responses);
    }

    @Override
    public RestResponse<List<StatisticOrderStatusResponse>> statisticOrderStatus(Instant startDate, Instant endDate) {
        List<StatisticOrderStatusResponse> responses = new ArrayList<>();
        Map<String, Long> orderStatus = orderServiceClient.getOrderTotalByStatus(startDate, endDate);
        orderStatus.forEach((key, value) -> responses.add(StatisticOrderStatusResponse.builder()
                .name(key)
                .value(value)
                .build()));
        return RestResponse.ok(responses);
    }

    @Override
    public RestResponse<List<StatisticReviewResponse>> statisticReview(Instant startDate, Instant endDate) {
        List<StatisticReviewResponse> responses = new ArrayList<>();
        Map<String, Long> review = userServiceClient.getReviewByRatingAndCreatedAt(startDate, endDate);
        review.forEach((key, value) -> responses.add(StatisticReviewResponse.builder()
                .name(key)
                .value(value)
                .build()));
        return RestResponse.ok(responses);
    }

    @Override
    public RestResponse<List<GetTop5OrderLatestResponse>> getTop5OrderLatest() {
        return RestResponse.ok(orderServiceClient.getTop5OrderLatest());
    }

    @Override
    public RestResponse<List<GetTop5TransactionLatestResponse>> getTop5TransactionLatest() {
        return RestResponse.ok(orderServiceClient.getTop5TransactionLatest());
    }

    @Override
    public RestResponse<List<GetTop5ReviewLatest>> getTop5ReviewLatest() {
        return RestResponse.ok(userServiceClient.getTop5ReviewLatest());
    }

    @Override
    public RestResponse<GetSaleLatestResponse> getSaleLatest() {
        return RestResponse.ok(productServiceClient.getSaleLatest());
    }

}

