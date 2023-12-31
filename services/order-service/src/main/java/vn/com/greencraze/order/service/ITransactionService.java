package vn.com.greencraze.order.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.dto.response.transaction.GetListTransactionResponse;
import vn.com.greencraze.order.dto.response.transaction.GetOneTransactionResponse;
import vn.com.greencraze.order.dto.response.transaction.GetTop5TransactionLatestResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface ITransactionService {

    RestResponse<ListResponse<GetListTransactionResponse>> getListTransaction(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all);

    List<GetTop5TransactionLatestResponse> getTop5TransactionLatest();

    RestResponse<GetOneTransactionResponse> getOneTransaction(Long id);

    BigDecimal getRevenue();

    BigDecimal getRevenueByCreatedAt(Instant startDate, Instant endDate);

}
