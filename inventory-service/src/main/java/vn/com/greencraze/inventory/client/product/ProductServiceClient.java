package vn.com.greencraze.inventory.client.product;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.greencraze.inventory.client.product.dto.request.ExportProductRequest;
import vn.com.greencraze.inventory.client.product.dto.request.ImportProductRequest;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@FeignClient("product-service")
public interface ProductServiceClient {

    String BASE = "/core/product";

    @PutMapping(BASE + "/products/import")
    void importProduct(@RequestBody @Valid ImportProductRequest request);

    @PutMapping(BASE + "/products/export")
    void exportProduct(@RequestBody @Valid ExportProductRequest request);

    @GetMapping(BASE + "/products/cost")
    Map<Long, BigDecimal> getListProductCost(@RequestParam Set<Long> ids);

}
