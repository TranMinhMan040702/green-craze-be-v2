package vn.com.greencraze.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.InvalidRequestException;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.dto.request.image.CreateProductImageRequest;
import vn.com.greencraze.product.dto.request.image.UpdateProductImageRequest;
import vn.com.greencraze.product.dto.response.image.CreateProductImageResponse;
import vn.com.greencraze.product.dto.response.image.GetListProductImageResponse;
import vn.com.greencraze.product.dto.response.image.GetOneProductImageResponse;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.ProductImage;
import vn.com.greencraze.product.mapper.ProductImageMapper;
import vn.com.greencraze.product.repository.ProductImageRepository;
import vn.com.greencraze.product.repository.ProductRepository;
import vn.com.greencraze.product.service.IProductImageService;
import vn.com.greencraze.product.service.IUploadService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements IProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final IUploadService uploadService;
    private final ProductImageMapper productImageMapper;

    private static final String RESOURCE_NAME = "ProductImage";

    @Override
    public RestResponse<List<GetListProductImageResponse>> getListProductImage(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        List<ProductImage> productImages = productImageRepository.findAllByProduct(product);
        return RestResponse.ok(productImageMapper.listProductImageToListGetListProductImageResponse(productImages));
    }

    @Override
    public RestResponse<GetOneProductImageResponse> getOneProductImage(Long id) {
        return productImageRepository.findById(id)
                .map(productImageMapper::productImageToGetOneProductImageResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<CreateProductImageResponse> createProductImage(CreateProductImageRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.productId()));
        return RestResponse.created(productImageMapper.productImageToCreateProductImageResponse(
                productImageRepository.save(ProductImage.builder()
                        .image(uploadService.uploadFile(request.image()))
                        .size((double) request.image().getSize())
                        .contentType(request.image().getContentType())
                        .product(product)
                        .build())
        ));
    }

    @Override
    public void updateProductImage(Long id, UpdateProductImageRequest request) {
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        productImage.setImage(uploadService.uploadFile(request.image()));
        productImage.setSize((double) request.image().getSize());
        productImage.setContentType(request.image().getContentType());
        productImageRepository.save(productImage);
    }

    @Override
    public void setDefaultProductImage(Long id, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        ProductImage productImageDefault = productImageRepository.findByProductAndIsDefault(product, true)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "isDefault", true));
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        productImageDefault.setIsDefault(false);
        productImageRepository.save(productImageDefault);
        productImage.setIsDefault(true);
        productImageRepository.save(productImage);
    }

    @Override
    public void deleteOneProductImage(Long id) {
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        if (productImage.getIsDefault()) {
            throw new InvalidRequestException("Cannot delete image default");
        }
        productImageRepository.deleteById(id);
    }

    @Override
    public void deleteListProductImage(List<Long> ids) {
        for (Long id : ids) {
            ProductImage productImage = productImageRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            if (!productImage.getIsDefault()) {
                productImageRepository.deleteById(id);
            }
        }
    }

}
