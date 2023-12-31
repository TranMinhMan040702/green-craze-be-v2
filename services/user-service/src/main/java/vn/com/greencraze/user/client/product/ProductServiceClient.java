package vn.com.greencraze.user.client.product;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.client.product.dto.request.UpdateListProductReviewRequest;
import vn.com.greencraze.user.client.product.dto.request.UpdateOneProductReviewRequest;
import vn.com.greencraze.user.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.user.client.product.dto.response.GetOneVariantResponse;

@FeignClient("product-service")
public interface ProductServiceClient {

    String BASE = "/core/product";

    @GetMapping(BASE + "/products/other/{id}")
    RestResponse<GetOneProductResponse> getOneProduct(@PathVariable Long id);

    @GetMapping(BASE + "/variants/other/{id}")
    RestResponse<GetOneVariantResponse> getOneVariant(@PathVariable Long id);

    @PutMapping(BASE + "/products/{id}/update-review")
    void updateProductReview(@PathVariable Long id, @RequestBody @Valid UpdateOneProductReviewRequest request);

    @PutMapping(BASE + "/products/update-list-review")
    void updateListProductReview(@RequestBody @Valid UpdateListProductReviewRequest request);


}
