package vn.com.greencraze.order.saga.createorder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import reactor.core.publisher.Mono;
import vn.com.greencraze.commons.domain.aggreate.CreateOrderAggregate;
import vn.com.greencraze.commons.domain.topic.Topics;
import vn.com.greencraze.commons.enumeration.OrderStatus;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.commons.messaging.Command;
import vn.com.greencraze.order.entity.Order;
import vn.com.greencraze.order.repository.OrderRepository;

import java.util.EnumSet;

@Slf4j
@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class CreateOrderSagaStateMachineConfig
        extends StateMachineConfigurerAdapter<CreateOrderSagaStates, CreateOrderSagaEvents> {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderRepository orderRepository;

    @Override
    public void configure(StateMachineConfigurationConfigurer<CreateOrderSagaStates, CreateOrderSagaEvents> config)
            throws Exception {
        config
                .withConfiguration()
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<CreateOrderSagaStates, CreateOrderSagaEvents> states)
            throws Exception {
        states
                .withStates()
                .initial(CreateOrderSagaStates.ORDER_CREATED)
                .state(CreateOrderSagaStates.ORDER_CREATED, reduceStock())
                .state(CreateOrderSagaStates.STOCK_REDUCED, createDocket())
                .state(CreateOrderSagaStates.STOCK_ERROR, cancelOrder())
                .state(CreateOrderSagaStates.DOCKET_CREATED, clearCart())
                .state(CreateOrderSagaStates.DOCKET_ERROR, revertStock())
                .state(CreateOrderSagaStates.STOCK_REVERTED, cancelOrder())
                .state(CreateOrderSagaStates.CART_CLEARED, completeOrder())
                .state(CreateOrderSagaStates.CART_ERROR, revertDocket())
                .state(CreateOrderSagaStates.DOCKET_REVERTED, revertStock())
                .end(CreateOrderSagaStates.ORDER_COMPLETED)
                .end(CreateOrderSagaStates.ORDER_CANCELED)
                .states(EnumSet.allOf(CreateOrderSagaStates.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<CreateOrderSagaStates, CreateOrderSagaEvents> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(CreateOrderSagaStates.ORDER_CREATED)
                .target(CreateOrderSagaStates.STOCK_REDUCED)
                .event(CreateOrderSagaEvents.REDUCE_STOCK)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.ORDER_CREATED)
                .target(CreateOrderSagaStates.STOCK_ERROR)
                .event(CreateOrderSagaEvents.CANCEL_STOCK)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.STOCK_ERROR)
                .target(CreateOrderSagaStates.ORDER_CANCELED)
                .event(CreateOrderSagaEvents.CANCEL_ORDER)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.STOCK_REDUCED)
                .target(CreateOrderSagaStates.DOCKET_CREATED)
                .event(CreateOrderSagaEvents.CREATE_DOCKET)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.STOCK_REDUCED)
                .target(CreateOrderSagaStates.DOCKET_ERROR)
                .event(CreateOrderSagaEvents.CANCEL_DOCKET)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.DOCKET_ERROR)
                .target(CreateOrderSagaStates.STOCK_REVERTED)
                .event(CreateOrderSagaEvents.REVERT_STOCK)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.STOCK_REVERTED)
                .target(CreateOrderSagaStates.ORDER_CANCELED)
                .event(CreateOrderSagaEvents.CANCEL_ORDER)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.DOCKET_CREATED)
                .target(CreateOrderSagaStates.CART_CLEARED)
                .event(CreateOrderSagaEvents.CLEAR_CART)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.DOCKET_CREATED)
                .target(CreateOrderSagaStates.CART_ERROR)
                .event(CreateOrderSagaEvents.CANCEL_CART)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.CART_ERROR)
                .target(CreateOrderSagaStates.DOCKET_REVERTED)
                .event(CreateOrderSagaEvents.REVERT_DOCKET)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.DOCKET_REVERTED)
                .target(CreateOrderSagaStates.STOCK_REVERTED)
                .event(CreateOrderSagaEvents.REVERT_STOCK)
                .and()
                .withExternal()
                .source(CreateOrderSagaStates.CART_CLEARED)
                .target(CreateOrderSagaStates.ORDER_COMPLETED)
                .event(CreateOrderSagaEvents.COMPLETE_ORDER);
    }

    @Bean
    public StateMachineListener<CreateOrderSagaStates, CreateOrderSagaEvents> listener() {
        return new StateMachineListenerAdapter<CreateOrderSagaStates, CreateOrderSagaEvents>() {
            @Override
            public void stateChanged(State<CreateOrderSagaStates, CreateOrderSagaEvents> from,
                                     State<CreateOrderSagaStates, CreateOrderSagaEvents> to) {
                log.info("State change to " + to.getId());
            }
        };
    }

    @Bean
    public Action<CreateOrderSagaStates, CreateOrderSagaEvents> reduceStock() {
        return ctx -> {
            CreateOrderAggregate aggregate = ctx.getExtendedState().get("aggregate", CreateOrderAggregate.class);
            // send command to product service handle reduce stock
            log.info("Send command to product service handle reduce stock " + aggregate.id());
            kafkaTemplate.send(Topics.REDUCE_STOCK,
                    String.valueOf(aggregate.id()),
                    new Command<>(aggregate.id(), aggregate));
        };
    }

    @Bean
    public Action<CreateOrderSagaStates, CreateOrderSagaEvents> revertDocket() {
        return ctx -> {
            CreateOrderAggregate aggregate = ctx.getExtendedState().get("aggregate", CreateOrderAggregate.class);
            // send command to inventory service handle revert docket
            log.info("Send command to inventory service handle revert docket " + aggregate.id());
            kafkaTemplate.send(Topics.REVERT_DOCKET,
                    String.valueOf(aggregate.id()),
                    new Command<>(aggregate.id(), aggregate));
        };
    }

    @Bean
    public Action<CreateOrderSagaStates, CreateOrderSagaEvents> createDocket() {
        return ctx -> {
            CreateOrderAggregate aggregate = ctx.getExtendedState().get("aggregate", CreateOrderAggregate.class);
            // send command to inventory service handle create docket
            log.info("Send command to inventory service handle create docket " + aggregate.id());
            kafkaTemplate.send(Topics.CREATE_DOCKET,
                    String.valueOf(aggregate.id()),
                    new Command<>(aggregate.id(), aggregate));
        };
    }

    @Bean
    public Action<CreateOrderSagaStates, CreateOrderSagaEvents> clearCart() {
        return ctx -> {
            CreateOrderAggregate aggregate = ctx.getExtendedState().get("aggregate", CreateOrderAggregate.class);
            // send command to user service handle clear cart
            log.info("Send command to user service handle clear cart " + aggregate.id());
            kafkaTemplate.send(Topics.CART_CLEAR,
                    String.valueOf(aggregate.id()),
                    new Command<>(aggregate.id(), aggregate));
        };
    }

    @Bean
    public Action<CreateOrderSagaStates, CreateOrderSagaEvents> cancelOrder() {
        return ctx -> {
            CreateOrderAggregate aggregate = ctx.getExtendedState().get("aggregate", CreateOrderAggregate.class);
            log.info("Cancel order " + aggregate.id());

            Order order = orderRepository.findById(aggregate.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", aggregate.id()));
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            ctx.getStateMachine().sendEvent(Mono.just(MessageBuilder.withPayload(CreateOrderSagaEvents.CANCEL_ORDER)
                    .build())).subscribe();
        };
    }

    @Bean
    public Action<CreateOrderSagaStates, CreateOrderSagaEvents> completeOrder() {
        return ctx -> {
            log.info("Complete order");
            ctx.getStateMachine()
                    .sendEvent(Mono.just(MessageBuilder.withPayload(CreateOrderSagaEvents.COMPLETE_ORDER)
                            .build())).subscribe();
        };
    }

    @Bean
    public Action<CreateOrderSagaStates, CreateOrderSagaEvents> revertStock() {
        return ctx -> {
            CreateOrderAggregate aggregate = ctx.getExtendedState().get("aggregate", CreateOrderAggregate.class);
            log.info("Send command to product service handle revert stock " + aggregate.id());
            // send command to product service handle revert stock
            kafkaTemplate.send(Topics.REVERT_STOCK,
                    String.valueOf(aggregate.id()),
                    new Command<>(aggregate.id(), aggregate));
        };
    }

}
