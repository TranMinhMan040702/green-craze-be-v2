package vn.com.greencraze.product.service;

import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.image.CreateProductImageRequest;
import vn.com.greencraze.product.dto.request.image.UpdateProductImageRequest;
import vn.com.greencraze.product.dto.response.image.CreateProductImageResponse;
import vn.com.greencraze.product.dto.response.image.GetListProductImageResponse;
import vn.com.greencraze.product.dto.response.image.GetOneProductImageResponse;

import java.util.List;

public interface IProductImageService {

    RestResponse<List<GetListProductImageResponse>> getListProductImage(Long productId);

    RestResponse<GetOneProductImageResponse> getOneProductImage(Long id);

    RestResponse<CreateProductImageResponse> createProductImage(CreateProductImageRequest request);

    void updateProductImage(Long id, UpdateProductImageRequest request);

    void setDefaultProductImage(Long id, Long productId);

    void deleteOneProductImage(Long id);

    void deleteListProductImage(List<Long> ids);

}
