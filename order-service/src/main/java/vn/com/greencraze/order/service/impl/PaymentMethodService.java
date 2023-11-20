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
import vn.com.greencraze.order.dto.request.paymentMethod.CreatePaymentMethodRequest;
import vn.com.greencraze.order.dto.request.paymentMethod.UpdatePaymentMethodRequest;
import vn.com.greencraze.order.dto.response.paymentMethod.CreatePaymentMethodResponse;
import vn.com.greencraze.order.dto.response.paymentMethod.GetListPaymentMethodResponse;
import vn.com.greencraze.order.dto.response.paymentMethod.GetOnePaymentMethodResponse;
import vn.com.greencraze.order.entity.PaymentMethod;
import vn.com.greencraze.order.mapper.PaymentMethodMapper;
import vn.com.greencraze.order.repository.PaymentMethodRepository;
import vn.com.greencraze.order.repository.specification.PaymentMethodSpecification;
import vn.com.greencraze.order.service.IPaymentMethodService;
import vn.com.greencraze.order.service.IUploadService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodService implements IPaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;
    private final IUploadService uploadService;

    private static final String RESOURCE_NAME = "PaymentMethod";
    private static final List<String> SEARCH_FIELDS = List.of("name", "code");

    @Override
    public RestResponse<ListResponse<GetListPaymentMethodResponse>> getListPaymentMethod(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    ) {
        PaymentMethodSpecification paymentMethodSpecification = new PaymentMethodSpecification();
        Specification<PaymentMethod> sortable = paymentMethodSpecification.sortable(isSortAscending, columnName);
        Specification<PaymentMethod> searchable = paymentMethodSpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListPaymentMethodResponse> responses = paymentMethodRepository
                .findAll(sortable.and(searchable), pageable)
                .map(paymentMethodMapper::paymentMethodToGetListPaymentMethodResponse);

        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOnePaymentMethodResponse> getOnePaymentMethod(Long id) {
        return paymentMethodRepository.findById(id)
                .map(paymentMethodMapper::paymentMethodToGetOnePaymentMethodResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<CreatePaymentMethodResponse> createPaymentMethod(CreatePaymentMethodRequest request) {
        PaymentMethod paymentMethod = paymentMethodMapper.createPaymentMethodRequestToPaymentMethod(request);
        paymentMethod.setImage(uploadService.uploadFile(request.image()));
        paymentMethodRepository.save(paymentMethod);

        return RestResponse.created(paymentMethodMapper.paymentMethodToCreatePaymentMethodResponse(paymentMethod));
    }

    @Override
    public void updatePaymentMethod(Long id, UpdatePaymentMethodRequest request) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .map(b -> paymentMethodMapper.updatePaymentMethodFromUpdatePaymentMethodRequest(b, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        if (request.image() != null) {
            paymentMethod.setImage(uploadService.uploadFile(request.image()));
        }
        paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public void deleteOnePaymentMethod(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        paymentMethod.setStatus(false);
        paymentMethodRepository.save(paymentMethod);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void deleteListPaymentMethod(List<Long> ids) {
        for (Long id : ids) {
            PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            paymentMethod.setStatus(false);
            paymentMethodRepository.save(paymentMethod);
        }
    }
}
