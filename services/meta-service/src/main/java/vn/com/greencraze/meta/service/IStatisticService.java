package vn.com.greencraze.meta.service;

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

import java.time.Instant;
import java.util.List;

public interface IStatisticService {

    RestResponse<StatisticTotalResponse> statisticTotal();

    RestResponse<List<StatisticRevenueExpenseResponse>> statisticRevenueExpense(int year);

    RestResponse<List<StatisticTopSellingProductResponse>> statisticTopSellingProduct(Instant startDate, Instant endDate);

    RestResponse<List<StatisticTopSellingProductYearResponse>> statisticTopSellingProductYear(int year);

    RestResponse<List<StatisticOrderStatusResponse>> statisticOrderStatus(Instant startDate, Instant endDate);

    RestResponse<List<StatisticReviewResponse>> statisticReview(Instant startDate, Instant endDate);

    // TODO: top 5 order latest
    RestResponse<List<GetTop5OrderLatestResponse>> getTop5OrderLatest();

    // TODO: top 5 transaction latest
    RestResponse<List<GetTop5TransactionLatestResponse>> getTop5TransactionLatest();

    // TODO: top 5 review latest
    RestResponse<List<GetTop5ReviewLatest>> getTop5ReviewLatest();

    // TODO: sale current
    RestResponse<GetSaleLatestResponse> getSaleLatest();

}
