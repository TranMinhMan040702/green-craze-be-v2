package vn.com.greencraze.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.order.dto.response.transaction.GetListTransactionResponse;
import vn.com.greencraze.order.dto.response.transaction.GetOneTransactionResponse;
import vn.com.greencraze.order.entity.Transaction;

@Mapper(uses = {ReferenceMapper.class})
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    Transaction idToTransaction(String id);

    @Mapping(source = "order.code", target = "orderCode")
    GetListTransactionResponse transactionToGetListTransactionResponse(Transaction transaction);

    @Mapping(source = "order.code", target = "orderCode")
    GetOneTransactionResponse transactionToGetOneTransactionResponse(Transaction transaction);

}
