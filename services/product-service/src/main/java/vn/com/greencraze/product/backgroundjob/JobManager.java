package vn.com.greencraze.product.backgroundjob;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.entity.Sale;
import vn.com.greencraze.product.repository.SaleRepository;
import vn.com.greencraze.product.service.ISaleService;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JobManager {

    private final TaskScheduler taskScheduler;

    private final SaleRepository saleRepository;

    public void handleSaleJobExecution(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", saleId));

        applySaleJobExecution(saleId, sale.getStartDate());
        cancelSaleJobExecution(saleId, sale.getEndDate());
    }

    public void applySaleJobExecution(Long saleId, Instant startTime) {
        ApplySaleJob applySaleJob = new ApplySaleJob();
        applySaleJob.setSaleId(saleId);

        taskScheduler.schedule(applySaleJob,
                startTime);
    }

    public void cancelSaleJobExecution(Long saleId, Instant startTime) {
        CancelSaleJob cancelSaleJob = new CancelSaleJob();
        cancelSaleJob.setSaleId(saleId);

        taskScheduler.schedule(cancelSaleJob,
                startTime);
    }
}
