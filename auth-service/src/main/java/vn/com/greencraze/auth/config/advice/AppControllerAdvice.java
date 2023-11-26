package vn.com.greencraze.auth.config.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.com.greencraze.auth.exception.ActiveUserException;
import vn.com.greencraze.auth.exception.BlockedUserException;
import vn.com.greencraze.auth.exception.ExistedUserException;
import vn.com.greencraze.auth.exception.InactivatedUserException;
import vn.com.greencraze.auth.exception.InvalidPasswordException;
import vn.com.greencraze.auth.exception.TokenCreationException;
import vn.com.greencraze.auth.exception.TokenValidationException;
import vn.com.greencraze.auth.exception.UnconfirmedUserException;
import vn.com.greencraze.commons.advice.CommonControllerAdvice;
import vn.com.greencraze.commons.advice.RestError;

import java.net.URI;

@RestControllerAdvice
public class AppControllerAdvice implements CommonControllerAdvice {

    @ExceptionHandler(ActiveUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError activeUserExceptionHandler(ActiveUserException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/active-user"))
                .title("Active User")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("ACTIVE_USER")
                .build();
    }

    @ExceptionHandler(InactivatedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError unactivatedUserExceptionHandler(InactivatedUserException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/unactivated-user"))
                .title("Unactivated User")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("UNACTIVATED_USER")
                .build();
    }

    @ExceptionHandler(BlockedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError blockedUserExceptionHandler(BlockedUserException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/blocked-user"))
                .title("Blocked User")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("BLOCKED_USER")
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

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError invalidPasswordExceptionHandler(InvalidPasswordException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/invalid-password"))
                .title("Invalid Password")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("INVALID_PASSWORD")
                .build();
    }

    @ExceptionHandler(ExistedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError existedUserExceptionHandler(ExistedUserException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/existed-user"))
                .title("Existed User")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("EXISTED_USER")
                .build();
    }

    @ExceptionHandler(TokenCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError tokenCreationExceptionHandler(TokenCreationException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/token-creation"))
                .title("Token Creation")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("TOKEN_CREATION")
                .build();
    }

    @ExceptionHandler(TokenValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError tokenValidationExceptionHandler(TokenValidationException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/token-validation"))
                .title("Token Validation")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("TOKEN_VALIDATION")
                .build();
    }

}
