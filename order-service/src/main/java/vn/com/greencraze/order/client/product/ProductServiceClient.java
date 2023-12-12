package vn.com.greencraze.order.client.product;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.client.product.dto.request.UpdateListProductQuantityRequest;
import vn.com.greencraze.order.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.order.client.product.dto.response.GetOneVariantResponse;

import java.util.List;
import java.util.Map;

@FeignClient("product-service")
public interface ProductServiceClient {

    String BASE = "/core/product";

    @GetMapping(BASE + "/products/other/{id}")
    RestResponse<GetOneProductResponse> getOneProduct(@PathVariable Long id);

    @GetMapping(BASE + "/products/other/variant/{variantId}")
    RestResponse<GetOneProductResponse> getOneProductByVariant(@PathVariable Long variantId);

    @GetMapping(BASE + "/variants/other/{id}")
    RestResponse<GetOneVariantResponse> getOneVariant(@PathVariable Long id);

    @PutMapping(BASE + "/products/update-quantity")
    void updateProductQuantity(@RequestBody @Valid UpdateListProductQuantityRequest request);

    @GetMapping(BASE + "/products/product-with-variant")
    Map<String, List<Long>> getListProductWithVariant();

}
