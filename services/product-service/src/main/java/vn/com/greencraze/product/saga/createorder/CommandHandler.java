package vn.com.greencraze.product.saga.createorder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.domain.aggreate.CreateOrderAggregate;
import vn.com.greencraze.commons.domain.id.KafkaGroupIds;
import vn.com.greencraze.commons.domain.topic.Topics;
import vn.com.greencraze.commons.messaging.Command;
import vn.com.greencraze.commons.messaging.Reply;
import vn.com.greencraze.product.service.IProductService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final IProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topics.REDUCE_STOCK, groupId = KafkaGroupIds.REDUCE_STOCK)
    public void handleReduceStockCommand(Command<Long, CreateOrderAggregate> command) {
        log.info("Received: {}", command);
        CreateOrderAggregate aggregate = objectMapper.convertValue(command.payload(), new TypeReference<>() {});

        try {
            productService.reduceStock(aggregate);
            kafkaTemplate.send(Topics.REDUCE_STOCK_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.success(aggregate.id(), aggregate));
        } catch (Exception e) {
            kafkaTemplate.send(Topics.REDUCE_STOCK_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.failure(aggregate.id(), aggregate));
        }
    }

    @KafkaListener(topics = Topics.REVERT_STOCK, groupId = KafkaGroupIds.REVERT_STOCK)
    public void handleRevertStockCommand(Command<Long, CreateOrderAggregate> command) {
        log.info("Received: {}", command);
        CreateOrderAggregate aggregate = objectMapper.convertValue(command.payload(), new TypeReference<>() {});

        try {
            productService.revertStock(aggregate);
            kafkaTemplate.send(Topics.REVERT_STOCK_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.success(aggregate.id(), aggregate));
        } catch (Exception e) {
            kafkaTemplate.send(Topics.REVERT_STOCK_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.failure(aggregate.id(), aggregate));
        }
    }

}
