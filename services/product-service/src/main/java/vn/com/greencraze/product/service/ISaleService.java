package vn.com.greencraze.product.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.sale.CreateSaleRequest;
import vn.com.greencraze.product.dto.request.sale.UpdateSaleRequest;
import vn.com.greencraze.product.dto.response.sale.CreateSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetListSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetSaleLatestResponse;

import java.util.List;

public interface ISaleService extends ISaleJobService {

    RestResponse<ListResponse<GetListSaleResponse>> getListSale(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all);

    RestResponse<GetOneSaleResponse> getOneSale(Long id);

    RestResponse<CreateSaleResponse> createSale(CreateSaleRequest request);

    void updateSale(Long id, UpdateSaleRequest request);

    void deleteOneSale(Long id);

    void deleteListSale(List<Long> ids);

    GetSaleLatestResponse getSaleLatest();

}
