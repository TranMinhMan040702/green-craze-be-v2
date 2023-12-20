package vn.com.greencraze.product.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.dto.request.variant.CreateVariantRequest;
import vn.com.greencraze.product.dto.request.variant.UpdateVariantRequest;
import vn.com.greencraze.product.dto.response.variant.CreateVariantResponse;
import vn.com.greencraze.product.dto.response.variant.GetListVariantResponse;
import vn.com.greencraze.product.dto.response.variant.GetOneVariantResponse;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.Variant;
import vn.com.greencraze.product.enumeration.VariantStatus;
import vn.com.greencraze.product.mapper.VariantMapper;
import vn.com.greencraze.product.repository.ProductRepository;
import vn.com.greencraze.product.repository.VariantRepository;
import vn.com.greencraze.product.service.IVariantService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements IVariantService {

    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final VariantMapper variantMapper;

    private static final String RESOURCE_NAME = "Variant";
    private static final List<String> SEARCH_FIELDS = List.of("name");

    @Override
    public RestResponse<List<GetListVariantResponse>> getListVariant(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        List<GetListVariantResponse> responses = variantMapper.listVariantToListGetListVariantResponse(
                variantRepository.findAllByProduct(product)
        );
        return RestResponse.ok(responses);
    }

    @Override
    public RestResponse<GetOneVariantResponse> getOneVariant(Long id) {
        return variantRepository.findById(id)
                .map(variantMapper::variantToGetOneVariantResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<CreateVariantResponse> creatVariant(CreateVariantRequest request) {
        if (!productRepository.existsById(request.productId())) {
            throw new ResourceNotFoundException("Product", "id", request.productId());
        }
        Variant variant = variantMapper.createVariantRequestToVariant(request);
        variant.setStatus(VariantStatus.ACTIVE);
        variantRepository.save(variant);
        return RestResponse.created(variantMapper.variantToCreateVariantResponse(variant));
    }

    @Override
    public void updateVariant(Long id, UpdateVariantRequest request) {
        variantRepository.findById(id)
                .map(v -> variantMapper.updateVariantFromUpdateVariantRequest(v, request))
                .map(variantRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public void deleteOneVariant(Long id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        variant.setStatus(VariantStatus.INACTIVE);
        variantRepository.save(variant);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void deleteListVariant(List<Long> ids) {
        for (Long id : ids) {
            Variant variant = variantRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            variant.setStatus(VariantStatus.INACTIVE);
            variantRepository.save(variant);
        }
    }

}
