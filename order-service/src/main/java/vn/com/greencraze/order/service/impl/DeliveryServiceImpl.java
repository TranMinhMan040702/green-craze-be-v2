package vn.com.greencraze.order.service.impl;

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
import vn.com.greencraze.order.dto.request.delivery.CreateDeliveryRequest;
import vn.com.greencraze.order.dto.request.delivery.UpdateDeliveryRequest;
import vn.com.greencraze.order.dto.response.delivery.CreateDeliveryResponse;
import vn.com.greencraze.order.dto.response.delivery.GetListDeliveryResponse;
import vn.com.greencraze.order.dto.response.delivery.GetOneDeliveryResponse;
import vn.com.greencraze.order.entity.Delivery;
import vn.com.greencraze.order.mapper.DeliveryMapper;
import vn.com.greencraze.order.repository.DeliveryRepository;
import vn.com.greencraze.order.repository.specification.DeliverySpecification;
import vn.com.greencraze.order.service.IDeliveryService;
import vn.com.greencraze.order.service.IUploadService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements IDeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final IUploadService uploadService;

    private static final String RESOURCE_NAME = "Delivery";
    private static final List<String> SEARCH_FIELDS = List.of("name", "code");

    @Override
    public RestResponse<ListResponse<GetListDeliveryResponse>> getListDelivery(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    ) {
        DeliverySpecification deliverySpecification = new DeliverySpecification();
        Specification<Delivery> sortable = deliverySpecification.sortable(isSortAscending, columnName);
        Specification<Delivery> searchable = deliverySpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListDeliveryResponse> responses = deliveryRepository
                .findAll(sortable.and(searchable), pageable)
                .map(deliveryMapper::deliveryToGetListDeliveryResponse);

        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneDeliveryResponse> getOneDelivery(Long id) {
        return deliveryRepository.findById(id)
                .map(deliveryMapper::deliveryToGetOneDeliveryResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<CreateDeliveryResponse> createDelivery(CreateDeliveryRequest request) {
        Delivery delivery = deliveryMapper.createDeliveryRequestToDelivery(request);
        delivery.setImage(uploadService.uploadFile(request.image()));
        deliveryRepository.save(delivery);
        return RestResponse.created(deliveryMapper.deliveryToCreateDeliveryResponse(delivery));
    }

    @Override
    public void updateDelivery(Long id, UpdateDeliveryRequest request) {
        Delivery delivery = deliveryRepository.findById(id)
                .map(b -> deliveryMapper.updateDeliveryFromUpdateDeliveryRequest(b, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        if (request.image() != null) {
            delivery.setImage(uploadService.uploadFile(request.image()));
        }
        deliveryRepository.save(delivery);
    }

    @Override
    public void deleteOneDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        delivery.setStatus(false);
        deliveryRepository.save(delivery);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void deleteListDelivery(List<Long> ids) {
        for (Long id : ids) {
            Delivery delivery = deliveryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            delivery.setStatus(false);
            deliveryRepository.save(delivery);
        }
    }

}
