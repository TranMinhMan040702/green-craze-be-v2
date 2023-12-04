package vn.com.greencraze.user.client.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.user.client.product.dto.request.UpdateListProductReviewRequest;
import vn.com.greencraze.user.client.product.dto.request.UpdateOneProductReviewRequest;
import vn.com.greencraze.user.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.user.client.product.dto.response.GetOneVariantResponse;

@FeignClient("product-service")
public interface ProductServiceClient {

    String BASE = "/core/product";

    @GetMapping(BASE + "/products/{id}")
    GetOneProductResponse getOneProduct(@PathVariable Long id);

    @PutMapping(BASE + "/products/{id}/update-review")
    void updateProductReview(@PathVariable Long id, @RequestBody UpdateOneProductReviewRequest request);

    @PutMapping(BASE + "/products/update-list-review")
    void updateListProductReview(@RequestBody UpdateListProductReviewRequest request);
    
    @GetMapping(BASE + "/variants/{id}")
    GetOneVariantResponse getOneVariant(@PathVariable Long id);

}
