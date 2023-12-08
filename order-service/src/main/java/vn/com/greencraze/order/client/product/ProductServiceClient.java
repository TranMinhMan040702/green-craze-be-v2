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

@FeignClient("product-service")
public interface ProductServiceClient {

    String BASE = "/core/product";

    @GetMapping(BASE + "/products/internal/{id}")
    RestResponse<GetOneProductResponse> getOneProduct(@PathVariable Long id);

    @GetMapping(BASE + "/variants/internal/{id}")
    RestResponse<GetOneVariantResponse> getOneVariant(@PathVariable Long id);

    @PutMapping(BASE + "/products/internal/update-quantity")
    void updateProductQuantity(@RequestBody @Valid UpdateListProductQuantityRequest request);

}
