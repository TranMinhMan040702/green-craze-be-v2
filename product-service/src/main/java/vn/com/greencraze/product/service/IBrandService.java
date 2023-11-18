package vn.com.greencraze.product.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.brand.CreateBrandRequest;
import vn.com.greencraze.product.dto.request.brand.UpdateBrandRequest;
import vn.com.greencraze.product.dto.response.brand.CreateBrandResponse;
import vn.com.greencraze.product.dto.response.brand.GetListBrandResponse;
import vn.com.greencraze.product.dto.response.brand.GetOneBrandResponse;

import java.util.List;

public interface IBrandService {

    RestResponse<ListResponse<GetListBrandResponse>> getListBrand(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    );

    RestResponse<GetOneBrandResponse> getOneBrand(Long id);

    RestResponse<CreateBrandResponse> createBrand(CreateBrandRequest request);

    void updateBrand(Long id, UpdateBrandRequest request);

    void deleteOneBrand(Long id);

    void deleteListBrand(List<Long> ids);

}
