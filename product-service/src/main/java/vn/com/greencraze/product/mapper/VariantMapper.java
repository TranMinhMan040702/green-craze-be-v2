package vn.com.greencraze.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.product.dto.request.variant.CreateVariantRequest;
import vn.com.greencraze.product.dto.request.variant.UpdateVariantRequest;
import vn.com.greencraze.product.dto.response.variant.CreateVariantResponse;
import vn.com.greencraze.product.dto.response.variant.GetListVariantResponse;
import vn.com.greencraze.product.dto.response.variant.GetOneVariantResponse;
import vn.com.greencraze.product.entity.Variant;

import java.util.List;

@Mapper(uses = {ReferenceMapper.class, ProductMapper.class})
public interface VariantMapper {

    @Mapping(source = "product.id", target = "productId")
    GetListVariantResponse variantToGetListVariantResponse(Variant variant);

    List<GetListVariantResponse> listVariantToListGetListVariantResponse(List<Variant> variants);

    @Mapping(source = "product.id", target = "productId")
    GetOneVariantResponse variantToGetOneVariantResponse(Variant variant);

    @Mapping(source = "productId", target = "product")
    Variant createVariantRequestToVariant(CreateVariantRequest createVariantRequest);

    @Mapping(source = "product.id", target = "productId")
    CreateVariantResponse variantToCreateVariantResponse(Variant variant);

    Variant updateVariantFromUpdateVariantRequest(
            @MappingTarget Variant variant, UpdateVariantRequest updateVariantRequest
    );

}
