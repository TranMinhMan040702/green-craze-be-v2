package vn.com.greencraze.meta.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.greencraze.meta.dto.response.GetTop5ReviewLatest;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@FeignClient("user-service")
public interface UserServiceClient {

    String BASE = "/core/user";

    @GetMapping(BASE + "/users/total")
    Long getTotalUser();

    @GetMapping(BASE + "/reviews/rating-created-at")
    Map<String, Long> getReviewByRatingAndCreatedAt(@RequestParam Instant startDate, @RequestParam Instant endDate);

    @GetMapping(BASE + "/reviews/top5-review-latest")
    List<GetTop5ReviewLatest> getTop5ReviewLatest();

}
