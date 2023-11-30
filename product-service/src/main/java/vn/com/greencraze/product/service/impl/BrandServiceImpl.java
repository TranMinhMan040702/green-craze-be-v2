package vn.com.greencraze.product.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.product.dto.request.brand.CreateBrandRequest;
import vn.com.greencraze.product.dto.request.brand.UpdateBrandRequest;
import vn.com.greencraze.product.dto.response.brand.CreateBrandResponse;
import vn.com.greencraze.product.dto.response.brand.GetListBrandResponse;
import vn.com.greencraze.product.dto.response.brand.GetOneBrandResponse;
import vn.com.greencraze.product.entity.Brand;
import vn.com.greencraze.product.mapper.BrandMapper;
import vn.com.greencraze.product.repository.BrandRepository;
import vn.com.greencraze.product.repository.specification.BrandSpecification;
import vn.com.greencraze.product.service.IBrandService;
import vn.com.greencraze.product.service.IUploadService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements IBrandService {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    private final IUploadService uploadService;

    private static final String RESOURCE_NAME = "Brand";
    private static final List<String> SEARCH_FIELDS = List.of("name", "description");

    @Override
    public RestResponse<ListResponse<GetListBrandResponse>> getListBrand(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    ) {
        BrandSpecification brandSpecification = new BrandSpecification();
        Specification<Brand> sortable = brandSpecification.sortable(isSortAscending, columnName);
        Specification<Brand> searchable = brandSpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListBrandResponse> responses = brandRepository
                .findAll(sortable.and(searchable), pageable)
                .map(brandMapper::brandToGetListBrandResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneBrandResponse> getOneBrand(Long id) {
        return brandRepository.findById(id)
                .map(brandMapper::brandToGetOneBrandResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<CreateBrandResponse> createBrand(CreateBrandRequest request) {
        Brand brand = brandMapper.createBrandRequestToBrand(request);
        brand.setImage(uploadService.uploadFile(request.image()));
        brandRepository.save(brand);
        return RestResponse.created(brandMapper.brandToCreateBrandResponse(brand));
    }

    @Override
    public void updateBrand(Long id, UpdateBrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .map(b -> brandMapper.updateBrandFromUpdateBrandRequest(b, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        if (request.image() != null) {
            brand.setImage(uploadService.uploadFile(request.image()));
        }
        brandRepository.save(brand);
    }

    @Override
    public void deleteOneBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        brand.setStatus(false);
        brandRepository.save(brand);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void deleteListBrand(List<Long> ids) {
        for (Long id : ids) {
            Brand brand = brandRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            brand.setStatus(false);
            brandRepository.save(brand);
        }
    }

}
