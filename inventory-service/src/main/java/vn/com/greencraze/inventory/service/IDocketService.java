package vn.com.greencraze.inventory.service;

import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.inventory.dto.request.CreateDocketRequest;
import vn.com.greencraze.inventory.dto.request.CreateDocketWithTypeExportRequest;
import vn.com.greencraze.inventory.dto.request.CreateDocketWithTypeImportRequest;
import vn.com.greencraze.inventory.dto.response.GetListDocketByProductResponse;

import java.util.List;

public interface IDocketService {

    void createDocketWithTypeImport(CreateDocketWithTypeImportRequest request);

    void createDocketWithTypeExport(CreateDocketWithTypeExportRequest request);

    RestResponse<List<GetListDocketByProductResponse>> getListDocketByProduct(Long id);

    // call from another service
    void createDocket(CreateDocketRequest request);

}
