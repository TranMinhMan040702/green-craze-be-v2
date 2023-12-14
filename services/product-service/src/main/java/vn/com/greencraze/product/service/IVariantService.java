package vn.com.greencraze.product.service;

import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.request.variant.CreateVariantRequest;
import vn.com.greencraze.product.dto.request.variant.UpdateVariantRequest;
import vn.com.greencraze.product.dto.response.variant.CreateVariantResponse;
import vn.com.greencraze.product.dto.response.variant.GetListVariantResponse;
import vn.com.greencraze.product.dto.response.variant.GetOneVariantResponse;

import java.util.List;

public interface IVariantService {

    RestResponse<List<GetListVariantResponse>> getListVariant(Long productId);

    RestResponse<GetOneVariantResponse> getOneVariant(Long id);

    RestResponse<CreateVariantResponse> creatVariant(CreateVariantRequest request);

    void updateVariant(Long id, UpdateVariantRequest request);

    void deleteOneVariant(Long id);

    void deleteListVariant(List<Long> ids);

}
