package vn.com.greencraze.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.product.dto.request.sale.CreateSaleRequest;
import vn.com.greencraze.product.dto.request.sale.UpdateSaleRequest;
import vn.com.greencraze.product.dto.response.sale.CreateSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetListSaleResponse;
import vn.com.greencraze.product.dto.response.sale.GetOneSaleResponse;
import vn.com.greencraze.product.entity.ProductCategory;
import vn.com.greencraze.product.entity.Sale;

@Mapper
public interface SaleMapper {

    GetListSaleResponse saleToGetListSaleResponse(Sale sale);

    GetOneSaleResponse saleToGetOneSaleResponse(Sale sale);

    @Mapping(target = "image", ignore = true)
    Sale createSaleRequestToSale(CreateSaleRequest createSaleRequest);

    CreateSaleResponse saleToCreateSaleResponse(Sale sale);

    @Mapping(source = "parentCategory.name", target = "parentName")
    CreateSaleResponse.ProductCategoryResponse productCategoryToProductCategoryResponse(
            ProductCategory productCategory);

    @Mapping(target = "image", ignore = true)
    Sale updateSaleFromUpdateSaleRequest(@MappingTarget Sale sale, UpdateSaleRequest updateSaleRequest);

}
