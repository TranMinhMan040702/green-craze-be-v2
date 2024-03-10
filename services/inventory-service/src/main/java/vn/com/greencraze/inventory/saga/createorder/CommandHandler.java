package vn.com.greencraze.inventory.saga.createorder;

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
import vn.com.greencraze.inventory.service.IDocketService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final IDocketService docketService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topics.CREATE_DOCKET, groupId = KafkaGroupIds.CREATE_DOCKET)
    public void handleCreateDocketCommand(Command<Long, CreateOrderAggregate> command) {
        log.info("Received: {}", command);
        CreateOrderAggregate aggregate = objectMapper.convertValue(command.payload(), new TypeReference<>() {});

        try {
            docketService.createDocket(aggregate);
            kafkaTemplate.send(Topics.CREATE_DOCKET_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.success(aggregate.id(), aggregate));
        } catch (Exception e) {
            kafkaTemplate.send(Topics.CREATE_DOCKET_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.failure(aggregate.id(), aggregate));
        }
    }

    @KafkaListener(topics = Topics.REVERT_DOCKET, groupId = KafkaGroupIds.REVERT_DOCKET)
    public void handleRevertDocketCommand(Command<Long, CreateOrderAggregate> command) {
        log.info("Received: {}", command);
        CreateOrderAggregate aggregate = objectMapper.convertValue(command.payload(), new TypeReference<>() {});

        try {
            docketService.revertDocket(aggregate);
            kafkaTemplate.send(Topics.REVERT_DOCKET_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.success(aggregate.id(), aggregate));
        } catch (Exception e) {
            kafkaTemplate.send(Topics.REVERT_DOCKET_REPLY,
                    String.valueOf(aggregate.id()),
                    Reply.failure(aggregate.id(), aggregate));
        }
    }

}
