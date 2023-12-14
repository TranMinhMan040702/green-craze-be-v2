package vn.com.greencraze.meta.client.inventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.Instant;

@FeignClient("inventory-service")
public interface InventoryServiceClient {

    String BASE = "/core/inventory";

    @GetMapping(BASE + "/dockets/expense")
    BigDecimal getExpense();

    @GetMapping(BASE + "/dockets/expense-by-created-at")
    BigDecimal getExpenseByCreatedAt(@RequestParam Instant startDate, @RequestParam Instant endDate);

}
