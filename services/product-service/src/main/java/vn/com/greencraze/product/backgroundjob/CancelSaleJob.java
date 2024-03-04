package vn.com.greencraze.product.backgroundjob;

import com.netflix.discovery.converters.Auto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.enumeration.SaleStatus;
import vn.com.greencraze.product.service.ISaleService;

import java.time.Instant;

@Component
public class  CancelSaleJob implements Runnable {

    private Long saleId;
    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }
    @Autowired
    private  ISaleService saleService;

    @Override
    public void run() {
        RestResponse<GetOneSaleResponse> sale = saleService.getOneSale(saleId);

        if(Instant.now().isAfter(sale.data().endDate())
            || sale.data().status() == SaleStatus.INACTIVE)
            return;

        saleService.cancelSale(saleId);
    }

}
