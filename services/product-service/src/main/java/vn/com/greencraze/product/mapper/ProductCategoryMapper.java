package vn.com.greencraze.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.product.dto.request.category.CreateProductCategoryRequest;
import vn.com.greencraze.product.dto.request.category.UpdateProductCategoryRequest;
import vn.com.greencraze.product.dto.response.category.CreateProductCategoryResponse;
import vn.com.greencraze.product.dto.response.category.GetListProductCategoryResponse;
import vn.com.greencraze.product.dto.response.category.GetOneProductCategoryBySlugResponse;
import vn.com.greencraze.product.dto.response.category.GetOneProductCategoryResponse;
import vn.com.greencraze.product.entity.ProductCategory;

@Mapper(uses = {ReferenceMapper.class})
public interface ProductCategoryMapper {

    @Mapping(target = "id", ignore = true)
    ProductCategory idToProductCategory(String id);

    @Mapping(source = "parentCategory.name", target = "parentName")
    GetListProductCategoryResponse productCategoryToGetListProductCategoryResponse(ProductCategory productCategory);

    @Mapping(source = "parentCategory.name", target = "parentName")
    GetOneProductCategoryResponse productCategoryToGetOneProductCategoryResponse(ProductCategory productCategory);

    @Mapping(source = "parentCategory.name", target = "parentName")
    GetOneProductCategoryBySlugResponse productCategoryToGetOneProductCategoryBySlugResponse(
            ProductCategory productCategory
    );

    @Mapping(source = "parentId", target = "parentCategory")
    @Mapping(target = "image", ignore = true)
    ProductCategory createProductCategoryRequestToProductCategory(
            CreateProductCategoryRequest createProductCategoryRequest
    );

    @Mapping(source = "parentCategory.name", target = "parentName")
    CreateProductCategoryResponse productCategoryToCreateProductCategoryResponse(ProductCategory productCategory);

    @Mapping(source = "parentId", target = "parentCategory")
    @Mapping(target = "image", ignore = true)
    ProductCategory updateProductCategoryFromUpdateProductCategoryRequest(
            @MappingTarget ProductCategory productCategory,
            UpdateProductCategoryRequest updateProductCategoryRequest
    );

}
