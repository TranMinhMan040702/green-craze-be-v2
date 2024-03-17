package vn.com.greencraze.order.saga.createorder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.domain.aggreate.CreateOrderAggregate;
import vn.com.greencraze.commons.domain.id.KafkaGroupIds;
import vn.com.greencraze.commons.domain.topic.Topics;
import vn.com.greencraze.commons.messaging.Reply;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplyHandler {

    private final CreateOrderSagaStateMachineManager stateMachineManager;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topics.REDUCE_STOCK_REPLY, groupId = KafkaGroupIds.REDUCE_STOCK_REPLY)
    public void handleReduceStockReply(Reply<Long, CreateOrderAggregate> reply) {
        log.info("Received: {}", reply);
        CreateOrderAggregate aggregate = objectMapper.convertValue(reply.payload(), new TypeReference<>() {});

        if (reply.success()) {
            stateMachineManager.sendEvent(aggregate.id(), CreateOrderSagaEvents.REDUCE_STOCK);
        } else {
            stateMachineManager.sendEvent(aggregate.id(), CreateOrderSagaEvents.CANCEL_STOCK);
        }
    }

    @KafkaListener(topics = Topics.CREATE_DOCKET_REPLY, groupId = KafkaGroupIds.CREATE_DOCKET_REPLY)
    public void handleCreateDocketReply(Reply<Long, CreateOrderAggregate> reply) {
        log.info("Received: {}", reply);
        CreateOrderAggregate aggregate = objectMapper.convertValue(reply.payload(), new TypeReference<>() {});

        if (reply.success()) {
            stateMachineManager.sendEvent(aggregate.id(), CreateOrderSagaEvents.CREATE_DOCKET);
        } else {
            stateMachineManager.sendEvent(aggregate.id(), CreateOrderSagaEvents.CANCEL_DOCKET);
        }
    }

    @KafkaListener(topics = Topics.CART_CLEAR_REPLY, groupId = KafkaGroupIds.CART_CLEAR_REPLY)
    public void handleClearCartReply(Reply<Long, CreateOrderAggregate> reply) {
        log.info("Received: {}", reply);
        CreateOrderAggregate aggregate = objectMapper.convertValue(reply.payload(), new TypeReference<>() {});

        if (reply.success()) {
            stateMachineManager.sendEvent(aggregate.id(), CreateOrderSagaEvents.CLEAR_CART);
        } else {
            stateMachineManager.sendEvent(aggregate.id(), CreateOrderSagaEvents.CANCEL_CART);
        }
    }

    @KafkaListener(topics = Topics.REVERT_STOCK_REPLY, groupId = KafkaGroupIds.REVERT_STOCK_REPLY)
    public void handleRevertStockReply(Reply<Long, CreateOrderAggregate> reply) {
        log.info("Received: {}", reply);
        CreateOrderAggregate aggregate = objectMapper.convertValue(reply.payload(), new TypeReference<>() {});

        if (reply.success()) {
            stateMachineManager.sendEvent(aggregate.id(), CreateOrderSagaEvents.REVERT_STOCK);
        }
    }

    @KafkaListener(topics = Topics.REVERT_DOCKET_REPLY, groupId = KafkaGroupIds.REVERT_DOCKET_REPLY)
    public void handleRevertDocketReply(Reply<Long, CreateOrderAggregate> reply) {
        log.info("Received: {}", reply);
        CreateOrderAggregate aggregate = objectMapper.convertValue(reply.payload(), new TypeReference<>() {});

        if (reply.success()) {
            stateMachineManager.sendEvent(aggregate.id(), CreateOrderSagaEvents.REVERT_DOCKET);
        }
    }

}
