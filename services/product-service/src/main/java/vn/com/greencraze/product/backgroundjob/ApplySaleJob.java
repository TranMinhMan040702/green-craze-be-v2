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
public class ApplySaleJob implements Runnable {

    private Long saleId;

    @Autowired
    private ISaleService saleService;

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    @Override
    public void run() {
        RestResponse<GetOneSaleResponse> sale = saleService.getOneSale(saleId);

        if(Instant.now().isBefore(sale.data().startDate())
            || sale.data().status() == SaleStatus.ACTIVE)
            return;

        saleService.applySale(saleId);
    }
}
