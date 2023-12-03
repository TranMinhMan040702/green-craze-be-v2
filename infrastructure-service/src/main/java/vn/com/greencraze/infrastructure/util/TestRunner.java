package vn.com.greencraze.infrastructure.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import vn.com.greencraze.amqp.RabbitMQMessageProducer;
import vn.com.greencraze.infrastructure.dto.request.SendEmailRequest;
import vn.com.greencraze.infrastructure.enumeration.EmailEvent;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final ApplicationContext context;
    private final RabbitMQMessageProducer producer;

    @Override
    public void run(String... args) throws Exception {
        context.getApplicationName();

        SendEmailRequest request = SendEmailRequest.builder()
                .email("nguyenminhson102002@gmail.com")
                .event(EmailEvent.CHANGE_PASSWORD)
                .payload(Map.of(
                        "fullname", "Tran Man",
                        "username", "tranman2002",
                        "link", "https://www.concretepage.com/java/java-9/java-map-of-and-map-ofentries"
                ))
                .build();

        producer.publish(request, "internal.exchange", "mail.routing_key");
    }

}
