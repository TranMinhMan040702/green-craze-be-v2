package vn.com.greencraze.product.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.category.CreateProductCategoryRequest;
import vn.com.greencraze.product.dto.request.category.UpdateProductCategoryRequest;
import vn.com.greencraze.product.dto.response.category.CreateProductCategoryResponse;
import vn.com.greencraze.product.dto.response.category.GetListProductCategoryResponse;
import vn.com.greencraze.product.dto.response.category.GetOneProductCategoryBySlugResponse;
import vn.com.greencraze.product.dto.response.category.GetOneProductCategoryResponse;

import java.util.List;

public interface IProductCategoryService {

    RestResponse<ListResponse<GetListProductCategoryResponse>> getListProductCategory(
            Integer page, Integer size, Boolean isSortAscending,
            String columnName, String search, Boolean all, Boolean status, Long parentCategoryId);

    RestResponse<GetOneProductCategoryResponse> getOneProductCategory(Long id);

    RestResponse<GetOneProductCategoryBySlugResponse> getOneProductCategoryBySlug(String slug);

    RestResponse<CreateProductCategoryResponse> createProductCategory(CreateProductCategoryRequest request);

    void updateProductCategory(Long id, UpdateProductCategoryRequest request);

    void deleteOneProductCategory(Long id);

    void deleteListProductCategory(List<Long> ids);

}
