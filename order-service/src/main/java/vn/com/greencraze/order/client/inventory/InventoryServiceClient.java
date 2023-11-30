package vn.com.greencraze.order.client.inventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.order.client.inventory.dto.request.CreateDocketRequest;

@FeignClient("inventory-service")
public interface InventoryServiceClient {

    String BASE = "/core/inventory";

    @PostMapping(BASE + "/inventories/create-docket")
    void createDocket(@RequestBody CreateDocketRequest request);

}