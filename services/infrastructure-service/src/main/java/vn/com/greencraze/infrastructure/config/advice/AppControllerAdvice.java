package vn.com.greencraze.infrastructure.config.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.com.greencraze.commons.advice.CommonControllerAdvice;
import vn.com.greencraze.commons.advice.RestError;
import vn.com.greencraze.infrastructure.exception.InvalidUserIdException;
import vn.com.greencraze.infrastructure.exception.SendEmailException;

import java.net.URI;

@RestControllerAdvice
public class AppControllerAdvice implements CommonControllerAdvice {

    @ExceptionHandler(SendEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError sendEmailExceptionHandler(SendEmailException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/send-email-error"))
                .title("Send Email Error")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("SEND_EMAIL_ERROR")
                .build();
    }

    @ExceptionHandler(InvalidUserIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError invalidUserIdExceptionHandler(InvalidUserIdException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/invalid-user-id"))
                .title("Invalid User Id")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("INVALID_USER_ID")
                .build();
    }

}
