package vn.com.greencraze.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.product.dto.request.brand.CreateBrandRequest;
import vn.com.greencraze.product.dto.request.brand.UpdateBrandRequest;
import vn.com.greencraze.product.dto.response.brand.CreateBrandResponse;
import vn.com.greencraze.product.dto.response.brand.GetListBrandResponse;
import vn.com.greencraze.product.dto.response.brand.GetOneBrandResponse;
import vn.com.greencraze.product.entity.Brand;

@Mapper(uses = {ReferenceMapper.class})
public interface BrandMapper {

    @Mapping(target = "id", ignore = true)
    Brand idToBrand(String id);

    GetListBrandResponse brandToGetListBrandResponse(Brand brand);

    GetOneBrandResponse brandToGetOneBrandResponse(Brand brand);

    @Mapping(target = "image", ignore = true)
    Brand createBrandRequestToBrand(CreateBrandRequest createBrandRequest);

    CreateBrandResponse brandToCreateBrandResponse(Brand brand);

    @Mapping(target = "image", ignore = true)
    Brand updateBrandFromUpdateBrandRequest(@MappingTarget Brand brand, UpdateBrandRequest updateBrandRequest);

}
