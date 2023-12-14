package vn.com.greencraze.infrastructure.service;

import vn.com.greencraze.infrastructure.dto.request.SendEmailRequest;

public interface IMailService {

    void sendEmail(SendEmailRequest request);

}
