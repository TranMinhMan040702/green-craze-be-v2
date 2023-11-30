package vn.com.greencraze.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.order.dto.response.transaction.GetListTransactionResponse;
import vn.com.greencraze.order.dto.response.transaction.GetOneTransactionResponse;
import vn.com.greencraze.order.entity.Transaction;
import vn.com.greencraze.order.mapper.TransactionMapper;
import vn.com.greencraze.order.repository.TransactionRepository;
import vn.com.greencraze.order.repository.specification.TransactionSpecification;
import vn.com.greencraze.order.service.ITransactionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    private static final String RESOURCE_NAME = "Transaction";
    private static final List<String> SEARCH_FIELDS = List.of("paymentMethod");

    @Override
    public RestResponse<ListResponse<GetListTransactionResponse>> getListTransaction(Integer page, Integer size
            , Boolean isSortAscending, String columnName, String search, Boolean all) {
        TransactionSpecification transactionSpecification = new TransactionSpecification();
        Specification<Transaction> sortable = transactionSpecification.sortable(isSortAscending, columnName);
        Specification<Transaction> searchable = transactionSpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListTransactionResponse> responses = transactionRepository
                .findAll(sortable.and(searchable), pageable)
                .map(transactionMapper::transactionToGetListTransactionResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<ListResponse<GetListTransactionResponse>> getTop5TransactionLatest() {
        TransactionSpecification transactionSpecification = new TransactionSpecification();
        Specification<Transaction> sortable = transactionSpecification.sortable(false, "createdAt");
        Pageable pageable = PageRequest.of(1, 5);
        Page<GetListTransactionResponse> responses = transactionRepository
                .findAll(sortable, pageable)
                .map(transactionMapper::transactionToGetListTransactionResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneTransactionResponse> getOneTransaction(Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::transactionToGetOneTransactionResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

}
