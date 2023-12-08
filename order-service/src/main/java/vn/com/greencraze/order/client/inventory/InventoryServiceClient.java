package vn.com.greencraze.order.client.inventory;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.order.client.inventory.dto.request.CreateDocketRequest;

@FeignClient("inventory-service")
public interface InventoryServiceClient {

    String BASE = "/core/inventory";

    @PostMapping(BASE + "/dockets/internal/create-docket")
    void createDocket(@RequestBody @Valid CreateDocketRequest request);

}
