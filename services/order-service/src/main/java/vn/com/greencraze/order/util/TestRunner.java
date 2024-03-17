package vn.com.greencraze.order.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final ApplicationContext context;

    //    private final StateMachineFactory<CreateOrderSagaStates, CreateOrderSagaEvents> stateMachineFactory;
    //    private final Map<Long, StateMachine<CreateOrderSagaStates, CreateOrderSagaEvents>> stateMachines =
    //            new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {
        context.getApplicationName();

        //        StateMachine<CreateOrderSagaStates, CreateOrderSagaEvents> stateMachine = stateMachineFactory.getStateMachine();
        //        stateMachine.startReactively().subscribe();

        //        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(CreateOrderSagaEvents.REDUCE_STOCK).build()))
        //                .subscribe();

        //        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(CreateOrderSagaEvents.CREATE_DOCKET).build()))
        //                .subscribe();

    }

}
