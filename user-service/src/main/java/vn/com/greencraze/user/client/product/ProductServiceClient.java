package vn.com.greencraze.user.client.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.com.greencraze.user.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.user.client.product.dto.response.GetOneVariantResponse;

@FeignClient("product-service")
public interface ProductServiceClient {
    String BASE = "/core/product";

    @GetMapping(BASE + "/products/{id}")
    GetOneProductResponse getOneProduct(@PathVariable Long id);

    @GetMapping(BASE + "/variants/{id}")
    GetOneVariantResponse getOneVariant(@PathVariable Long id);
}
