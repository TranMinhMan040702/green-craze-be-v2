package vn.com.greencraze.order.cronjob;

import lombok.Builder;
import vn.com.greencraze.order.service.IJobService;

@Builder
public class CancelOrderJob implements Runnable {

    private Long orderId;

    private final IJobService jobService;

    @Override
    public void run() {
        jobService.cancelOrderJob(orderId);
    }

}
