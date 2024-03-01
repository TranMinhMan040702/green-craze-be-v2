package vn.com.greencraze.product.backgroundjob;

import lombok.Builder;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.enumeration.SaleStatus;
import vn.com.greencraze.product.service.ISaleService;

import java.time.Instant;

@Builder
public class  CancelSaleJob implements Runnable {

    private Long saleId;
    private final ISaleService saleService;

    @Override
    public void run() {
        RestResponse<GetOneSaleResponse> sale = saleService.getOneSale(saleId);

        if(Instant.now().isAfter(sale.data().endDate())
            || sale.data().status() == SaleStatus.INACTIVE)
            return;

        saleService.cancelSale(saleId);
    }
}
