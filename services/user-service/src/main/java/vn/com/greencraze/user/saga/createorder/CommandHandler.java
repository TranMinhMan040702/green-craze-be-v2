package vn.com.greencraze.user.saga.createorder;

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
import vn.com.greencraze.user.service.ICartService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final ICartService cartService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topics.CART_CLEAR, groupId = KafkaGroupIds.CART_CLEAR)
    public void handleClearCartCommand(Command<Long, CreateOrderAggregate> command) {
        log.info("Received: {}", command);
        CreateOrderAggregate aggregate = objectMapper.convertValue(command.payload(), new TypeReference<>() {});

        try {
            cartService.clearCart(aggregate);
            kafkaTemplate.send(Topics.CART_CLEAR_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.success(aggregate.id(), aggregate));
        } catch (Exception e) {
            kafkaTemplate.send(Topics.CART_CLEAR_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.failure(aggregate.id(), aggregate));
        }

    }

}
