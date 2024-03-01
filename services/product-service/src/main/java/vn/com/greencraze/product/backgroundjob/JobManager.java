package vn.com.greencraze.product.backgroundjob;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.service.ISaleService;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JobManager {

    private final TaskScheduler taskScheduler;
    private final ISaleService saleService;

    public void handleSaleJobExecution(Long saleId) {
        RestResponse<GetOneSaleResponse> sale = saleService.getOneSale(saleId);

        applySaleJobExecution(saleId, sale.data().startDate());
        cancelSaleJobExecution(saleId, sale.data().endDate());
    }

    public void applySaleJobExecution(Long saleId, Instant startTime) {
        taskScheduler.schedule(ApplySaleJob.builder()
                        .saleId(saleId)
                        .saleService(saleService)
                        .build(),
                startTime);
    }

    public void cancelSaleJobExecution(Long saleId, Instant startTime) {
        taskScheduler.schedule(CancelSaleJob.builder()
                        .saleId(saleId)
                        .saleService(saleService)
                        .build(),
                startTime);
    }
}
