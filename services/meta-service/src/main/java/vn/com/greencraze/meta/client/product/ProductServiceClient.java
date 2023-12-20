package vn.com.greencraze.meta.client.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vn.com.greencraze.meta.dto.response.GetSaleLatestResponse;

@FeignClient("product-service")
public interface ProductServiceClient {

    String BASE = "/core/product";

    @GetMapping(BASE + "/sales/sale-latest")
    GetSaleLatestResponse getSaleLatest();

}
