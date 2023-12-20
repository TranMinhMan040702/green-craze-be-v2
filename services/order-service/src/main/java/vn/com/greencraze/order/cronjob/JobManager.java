package vn.com.greencraze.order.cronjob;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import vn.com.greencraze.order.service.IJobService;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JobManager {

    private final TaskScheduler taskScheduler;
    private final IJobService jobService;

    public void execute(Long orderId) {
        taskScheduler.schedule(CancelOrderJob.builder()
                        .orderId(orderId)
                        .jobService(jobService)
                        .build(),
                Instant.now().plusSeconds(60 * 5));
    }

}
