package vn.com.greencraze.product.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.product.CreateProductRequest;
import vn.com.greencraze.product.dto.request.product.ExportProductRequest;
import vn.com.greencraze.product.dto.request.product.ImportProductRequest;
import vn.com.greencraze.product.dto.request.product.UpdateProductRequest;
import vn.com.greencraze.product.dto.response.product.CreateProductResponse;
import vn.com.greencraze.product.dto.response.product.GetListProductResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductBySlugResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    RestResponse<ListResponse<GetListProductResponse>> getListProduct(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all,
            String categorySlug, BigDecimal minPrice, BigDecimal maxPrice, Long brandId
    );

    RestResponse<GetOneProductResponse> getOneProduct(Long id);

    RestResponse<GetOneProductBySlugResponse> getOneProductBySlug(String slug);

    RestResponse<CreateProductResponse> createProduct(CreateProductRequest request) throws Exception;

    void updateProduct(Long id, UpdateProductRequest request);

    void deleteOneProduct(Long id);

    void deleteListProduct(List<Long> ids);

    void importProduct(ImportProductRequest request);

    void exportProduct(ExportProductRequest request);

}