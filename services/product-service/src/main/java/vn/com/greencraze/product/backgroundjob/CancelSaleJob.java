package vn.com.greencraze.product.backgroundjob;

import lombok.Builder;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.entity.Sale;
import vn.com.greencraze.product.enumeration.SaleStatus;
import vn.com.greencraze.product.repository.SaleRepository;
import vn.com.greencraze.product.service.ISaleJobService;

import java.time.Instant;

@Builder
public class CancelSaleJob implements Runnable {

    private Long saleId;
    private final ISaleJobService saleJobService;
    private final SaleRepository saleRepository;

    @Override
    public void run() {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", saleId));


        if (Instant.now().isAfter(sale.getEndDate()) || sale.getStatus() == SaleStatus.INACTIVE) {
            return;
        }
        saleJobService.cancelSale(saleId);
    }

}
