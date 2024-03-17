package vn.com.greencraze.product.backgroundjob;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.entity.Sale;
import vn.com.greencraze.product.repository.SaleRepository;
import vn.com.greencraze.product.service.ISaleJobService;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JobManager {

    private final TaskScheduler taskScheduler;

    @Qualifier("saleJobServiceImpl")
    private final ISaleJobService saleJobService;

    private final SaleRepository saleRepository;

    public void handleSaleJobExecution(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", saleId));

        applySaleJobExecution(saleId, sale.getStartDate());
        cancelSaleJobExecution(saleId, sale.getEndDate());
    }

    public void applySaleJobExecution(Long saleId, Instant startTime) {
        taskScheduler.schedule(ApplySaleJob.builder()
                .saleId(saleId)
                .saleJobService(saleJobService)
                .build(), startTime);
    }

    public void cancelSaleJobExecution(Long saleId, Instant startTime) {
        taskScheduler.schedule(CancelSaleJob.builder()
                .saleId(saleId)
                .saleJobService(saleJobService)
                .build(), startTime);
    }

}
