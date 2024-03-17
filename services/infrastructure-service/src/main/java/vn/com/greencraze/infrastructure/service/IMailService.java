package vn.com.greencraze.infrastructure.service;

import vn.com.greencraze.commons.domain.dto.SendEmailRequest;

public interface IMailService {

    void sendEmail(SendEmailRequest request);

}
