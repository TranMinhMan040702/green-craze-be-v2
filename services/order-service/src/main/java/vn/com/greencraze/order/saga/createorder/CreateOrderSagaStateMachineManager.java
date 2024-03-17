package vn.com.greencraze.order.saga.createorder;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.com.greencraze.commons.domain.aggreate.CreateOrderAggregate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class CreateOrderSagaStateMachineManager {

    // use to create statemachine
    private final StateMachineFactory<CreateOrderSagaStates, CreateOrderSagaEvents> stateMachineFactory;

    // manager all statemachine in app
    private final Map<Long, StateMachine<CreateOrderSagaStates, CreateOrderSagaEvents>> stateMachines =
            new ConcurrentHashMap<>();

    public void putSaga(CreateOrderAggregate aggregate) {
        // create new statemachine
        StateMachine<CreateOrderSagaStates, CreateOrderSagaEvents> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.getExtendedState().getVariables().put("aggregate", aggregate);
        stateMachines.put(aggregate.id(), stateMachine);
        stateMachine.startReactively().subscribe();
    }

    public void sendEvent(Long id, CreateOrderSagaEvents event) {
        stateMachines.get(id).sendEvent(Mono.just(MessageBuilder.withPayload(event).build())).subscribe();
    }

}
