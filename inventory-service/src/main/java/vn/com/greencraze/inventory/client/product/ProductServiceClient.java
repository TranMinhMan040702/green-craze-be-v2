package vn.com.greencraze.inventory.client.product;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.inventory.client.product.dto.ExportProductRequest;
import vn.com.greencraze.inventory.client.product.dto.ImportProductRequest;

@FeignClient("product-service")
public interface ProductServiceClient {

    String BASE = "/core/product";

    @PutMapping(BASE + "/products/import")
    void importProduct(@RequestBody @Valid ImportProductRequest request);

    @PutMapping(BASE + "/products/export")
    void exportProduct(@RequestBody @Valid ExportProductRequest request);

}
