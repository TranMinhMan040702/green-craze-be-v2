package vn.com.greencraze.order.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.dto.response.transaction.GetListTransactionResponse;
import vn.com.greencraze.order.dto.response.transaction.GetOneTransactionResponse;

public interface ITransactionService {
    RestResponse<ListResponse<GetListTransactionResponse>> getListTransaction(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    );

    RestResponse<ListResponse<GetListTransactionResponse>> getTop5TransactionLatest();

    RestResponse<GetOneTransactionResponse> getOneTransaction(Long id);
}
