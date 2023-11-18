package vn.com.greencraze.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.product.dto.request.product.CreateProductRequest;
import vn.com.greencraze.product.dto.request.product.UpdateProductRequest;
import vn.com.greencraze.product.dto.response.product.CreateProductResponse;
import vn.com.greencraze.product.dto.response.product.GetListProductResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductBySlugResponse;
import vn.com.greencraze.product.dto.response.product.GetOneProductResponse;
import vn.com.greencraze.product.entity.Product;

@Mapper(uses = {ReferenceMapper.class, ProductCategoryMapper.class, BrandMapper.class, UnitMapper.class})
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product idToProduct(String id);

    GetListProductResponse productToGetListProductResponse(Product product);

    GetOneProductResponse productToGetOneProductResponse(Product product);

    GetOneProductBySlugResponse productToGetOneProductBySlugResponse(Product product);

    @Mapping(source = "categoryId", target = "productCategory")
    @Mapping(source = "brandId", target = "brand")
    @Mapping(source = "unitId", target = "unit")
    @Mapping(target = "variants", ignore = true)
    Product createProductRequestToProduct(CreateProductRequest createProductRequest);

    CreateProductResponse productToCreateProductResponse(Product product);

    @Mapping(source = "categoryId", target = "productCategory")
    @Mapping(source = "brandId", target = "brand")
    @Mapping(source = "unitId", target = "unit")
    Product updateProductFromUpdateProductRequest(
            @MappingTarget Product product, UpdateProductRequest updateProductRequest
    );

}
