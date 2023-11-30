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
        //                .email("codexamxi@gmail.com")
        //                .event(EmailEvent.CHANGE_PASSWORD)
        //                .payload(Map.of(
        //                        "fullname", "Tran Man",
        //                        "username", "tranman2002",
        //                        "link", "https://www.concretepage.com/java/java-9/java-map-of-and-map-ofentries"
        //                ))
        //                .build();
        //        mailService.sendEmail(request);
    }

}
