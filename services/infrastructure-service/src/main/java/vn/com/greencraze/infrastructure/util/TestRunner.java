package vn.com.greencraze.infrastructure.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import vn.com.greencraze.infrastructure.service.IMailService;

@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final ApplicationContext context;
    private final IMailService mailService;

    @Override
    public void run(String... args) throws Exception {
        context.getApplicationName();

        //        SendEmailRequest request = SendEmailRequest.builder()
        //                .email("nguyenminhson102002@gmail.com")
        //                .event(EmailEvent.ORDER_CONFIRMATION)
        //                .payload(Map.of(
        //                        "name", "Tran Man",
        //                        "email", "tranman2002@gmail.com",
        //                        "receiver", "Tran Man",
        //                        "phone", "0978678677",
        //                        "totalPrice", 10900,
        //                        "paymentMethod", "Paypal",
        //                        "address", "KTX khu B"
        //                ))
        //                .build();
        //        //        SendEmailRequest request = SendEmailRequest.builder()
        //        //                .email("mantm040702@gmail.com")
        //        //                .event(EmailEvent.ORDER_CONFIRMATION)
        //        //                .payload(Map.of(
        //        //                        "name", "Tran Man",
        //        //                        "email", "tranman2002@gmail.com",
        //        //                        "receiver", "Tran Man",
        //        //                        "phone", "0978678677",
        //        //                        "totalPrice", 10900,
        //        //                        "paymentMethod", "Paypal",
        //        //                        "address", "KTX khu B"
        //        //                ))
        //        //                .build();
        //
        //        mailService.sendEmail(request);

        //
        //        producer.publish(request, "internal.exchange", "mail.routing_key");
    }

}
