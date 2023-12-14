package vn.com.greencraze.meta.client.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.greencraze.meta.dto.response.GetTop5OrderLatestResponse;
import vn.com.greencraze.meta.dto.response.GetTop5TransactionLatestResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@FeignClient("order-service")
public interface OrderServiceClient {

    String BASE = "/core/order";

    @GetMapping(BASE + "/orders/total/status/delivered")
    Long getTotalOrderWithStatusDelivered();

    @GetMapping(BASE + "/orders/top-selling-product")
    Map<String, Long> getTopSellingProduct(@RequestParam Instant startDate, @RequestParam Instant endDate);

    @GetMapping(BASE + "/orders/order-total-by-status")
    Map<String, Long> getOrderTotalByStatus(@RequestParam Instant startDate, @RequestParam Instant endDate);

    @GetMapping(BASE + "/orders/top5-order-latest")
    List<GetTop5OrderLatestResponse> getTop5OrderLatest();

    @GetMapping(BASE + "/transactions/revenue")
    BigDecimal getRevenue();

    @GetMapping(BASE + "/transactions/revenue-by-created-at")
    BigDecimal getRevenueByCreatedAt(@RequestParam Instant startDate, @RequestParam Instant endDate);

    @GetMapping(BASE + "/transactions/top5-transaction-latest")
    List<GetTop5TransactionLatestResponse> getTop5TransactionLatest();

}
