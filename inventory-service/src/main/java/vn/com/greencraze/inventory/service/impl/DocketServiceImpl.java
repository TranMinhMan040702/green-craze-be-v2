package vn.com.greencraze.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.inventory.client.product.ProductServiceClient;
import vn.com.greencraze.inventory.client.product.dto.request.ExportProductRequest;
import vn.com.greencraze.inventory.client.product.dto.request.ImportProductRequest;
import vn.com.greencraze.inventory.dto.request.CreateDocketRequest;
import vn.com.greencraze.inventory.dto.request.CreateDocketWithTypeExportRequest;
import vn.com.greencraze.inventory.dto.request.CreateDocketWithTypeImportRequest;
import vn.com.greencraze.inventory.dto.response.GetListDocketByProductResponse;
import vn.com.greencraze.inventory.entity.Docket;
import vn.com.greencraze.inventory.enumeration.DocketType;
import vn.com.greencraze.inventory.mapper.DocketMapper;
import vn.com.greencraze.inventory.repository.DocketRepository;
import vn.com.greencraze.inventory.service.IDocketService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocketServiceImpl implements IDocketService {

    private final DocketRepository docketRepository;

    private final DocketMapper docketMapper;

    private final ProductServiceClient productServiceClient;

    private static final String RESOURCE_NAME = "Docket";

    @Override
    public void createDocketWithTypeImport(CreateDocketWithTypeImportRequest request) {
        Docket docket = Docket.builder()
                .orderId(null)
                .productId(request.productId())
                .quantity(request.quantity())
                .type(DocketType.IMPORT)
                .code(UUID.randomUUID().toString()) // TODO
                .note(request.note())
                .build();
        docketRepository.save(docket);

        productServiceClient.importProduct(ImportProductRequest.builder()
                .id(request.productId())
                .quantity(request.quantity())
                .actualInventory(request.actualInventory())
                .build());
    }

    @Override
    public void createDocketWithTypeExport(CreateDocketWithTypeExportRequest request) {
        Docket docket = Docket.builder()
                .orderId(request.orderId())
                .productId(request.productId())
                .quantity(request.quantity())
                .type(DocketType.EXPORT)
                .code(UUID.randomUUID().toString()) // TODO
                .note(request.note())
                .build();
        docketRepository.save(docket);

        productServiceClient.exportProduct(ExportProductRequest.builder()
                .id(request.productId())
                .quantity(request.quantity())
                .build());
    }

    @Override
    public RestResponse<List<GetListDocketByProductResponse>> getListDocketByProduct(Long id) {
        List<GetListDocketByProductResponse> responses = docketRepository.findByProductId(id)
                .stream().map(docketMapper::docketToGetListDocketByProductResponse).toList();
        return RestResponse.ok(responses);
    }

    @Override
    public void createDocket(CreateDocketRequest request) {
        for (CreateDocketRequest.ProductDocket productDocket : request.productDockets()) {
            Docket docket = Docket.builder()
                    .orderId(request.orderId())
                    .productId(productDocket.productId())
                    .quantity(productDocket.quantity().longValue())
                    .type(DocketType.valueOf(request.type()))
                    .code(UUID.randomUUID().toString()) // TODO
                    .build();
            docketRepository.save(docket);
        }
    }

    @Override
    public BigDecimal getExpense() {
        List<Docket> dockets = docketRepository.findAllByType(DocketType.IMPORT);
        Set<Long> productIds = dockets.stream()
                .map(Docket::getProductId)
                .collect(Collectors.toSet());

        Map<Long, BigDecimal> productCost = productServiceClient.getListProductCost(productIds);

        return dockets.stream()
                .map(docket -> productCost.get(docket.getProductId())
                        .multiply(BigDecimal.valueOf(docket.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getExpenseByCreatedAt(Instant startDate, Instant endDate) {
        List<Docket> dockets = docketRepository.findAllByTypeAndCreatedAtBetween(DocketType.IMPORT, startDate, endDate);
        Set<Long> productIds = dockets.stream()
                .map(Docket::getProductId)
                .collect(Collectors.toSet());

        Map<Long, BigDecimal> productCost = productServiceClient.getListProductCost(productIds);

        return dockets.stream()
                .map(docket -> productCost.get(docket.getProductId())
                        .multiply(BigDecimal.valueOf(docket.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
