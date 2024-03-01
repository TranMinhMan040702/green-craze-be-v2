package vn.com.greencraze.product.backgroundjob;

import lombok.Builder;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.enumeration.SaleStatus;
import vn.com.greencraze.product.service.ISaleService;

import java.time.Instant;

@Builder
public class ApplySaleJob implements Runnable {

    private Long saleId;
    private final ISaleService saleService;

    @Override
    public void run() {
        RestResponse<GetOneSaleResponse> sale = saleService.getOneSale(saleId);

        if(Instant.now().isBefore(sale.data().startDate())
            || sale.data().status() == SaleStatus.ACTIVE)
            return;

        saleService.applySale(saleId);
    }
}
