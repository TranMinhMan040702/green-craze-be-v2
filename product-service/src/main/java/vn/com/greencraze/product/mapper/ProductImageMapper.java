package vn.com.greencraze.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.product.dto.response.image.CreateProductImageResponse;
import vn.com.greencraze.product.dto.response.image.GetListProductImageResponse;
import vn.com.greencraze.product.dto.response.image.GetOneProductImageResponse;
import vn.com.greencraze.product.entity.ProductImage;

import java.util.List;

@Mapper(uses = {ReferenceMapper.class, ProductMapper.class})
public interface ProductImageMapper {

    @Mapping(source = "product.id", target = "productId")
    GetListProductImageResponse productImageToGetListProductImageResponse(ProductImage productImage);

    List<GetListProductImageResponse> listProductImageToListGetListProductImageResponse(
            List<ProductImage> productImages
    );

    @Mapping(source = "product.id", target = "productId")
    GetOneProductImageResponse productImageToGetOneProductImageResponse(ProductImage productImage);

    @Mapping(source = "product.id", target = "productId")
    CreateProductImageResponse productImageToCreateProductImageResponse(ProductImage productImage);

}
