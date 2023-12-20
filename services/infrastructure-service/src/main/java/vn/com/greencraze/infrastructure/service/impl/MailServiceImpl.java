package vn.com.greencraze.infrastructure.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vn.com.greencraze.infrastructure.dto.request.SendEmailRequest;
import vn.com.greencraze.infrastructure.exception.SendEmailException;
import vn.com.greencraze.infrastructure.service.IMailService;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements IMailService {


    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;
    private static final String MAIL_TEMPLATE_PREFIX = "mail/";

    @Override
    public void sendEmail(SendEmailRequest request) {
        var context = new Context();
        context.setVariables(request.payload());
        String content = templateEngine.process(MAIL_TEMPLATE_PREFIX + request.event().template(), context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper mimeHelper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            mimeHelper.setFrom("greencraze@gmail.com (Green Craze System)");
            mimeHelper.setTo(request.email());
            mimeHelper.setSubject(request.event().subject());
            mimeHelper.setText(content, true);

            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new SendEmailException("Unable to send email");
        }
    }

}
