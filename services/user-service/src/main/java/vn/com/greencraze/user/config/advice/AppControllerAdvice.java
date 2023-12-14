package vn.com.greencraze.user.config.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.com.greencraze.commons.advice.CommonControllerAdvice;
import vn.com.greencraze.commons.advice.RestError;
import vn.com.greencraze.user.exception.InactivatedUserException;
import vn.com.greencraze.user.exception.UnconfirmedUserException;

import java.net.URI;

@RestControllerAdvice
public class AppControllerAdvice implements CommonControllerAdvice {

    @ExceptionHandler(InactivatedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError inactivatedUserExceptionHandler(InactivatedUserException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/inactivated-user"))
                .title("Inactivated User")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("INACTIVATED_USER")
                .build();
    }

    @ExceptionHandler(UnconfirmedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError unconfirmedUserExceptionHandler(UnconfirmedUserException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/unconfirmed-user"))
                .title("Unconfirmed User")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("UNCONFIRMED_USER")
                .build();
    }

}
