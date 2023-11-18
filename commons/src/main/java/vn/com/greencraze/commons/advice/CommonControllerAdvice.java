package vn.com.greencraze.commons.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import vn.com.greencraze.commons.exception.InvalidRequestException;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommonControllerAdvice {

    Logger log = LoggerFactory.getLogger(CommonControllerAdvice.class);

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError accessDeniedExceptionHandler(AccessDeniedException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .type(URI.create("https://problems.greencraze.com.vn/access-denied"))
                .title("Access denied")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("ACCESS_DENIED")
                .build();
    }

    //    @ExceptionHandler(AuthenticationException.class)
    //    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    //    default RestError authenticationExceptionHandler(AuthenticationException e, HttpServletRequest request) {
    //        return RestError.builder()
    //                .status(HttpStatus.UNAUTHORIZED.value())
    //                .type(URI.create("https://problems.affina.com.vn/authentication-error"))
    //                .title("Authentication error")
    //                .detail(e.getMessage())
    //                .instance(URI.create(request.getRequestURI()))
    //                .code("AUTHENTICATION_ERROR")
    //                .build();
    //    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError resourceNotFoundExceptionHandler(ResourceNotFoundException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .type(URI.create("https://problems.greencraze.com.vn/resource-not-found"))
                .title("Resource not found")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("RESOURCE_NOT_FOUND")
                .build();
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError invalidRequestExceptionHandler(InvalidRequestException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/invalid-request"))
                .title("Invalid Request")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("INVALID_REQUEST")
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError noHandlerFoundExceptionHandler(NoHandlerFoundException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .type(URI.create("https://problems.greencraze.com.vn/endpoint-not-found"))
                .title("Endpoint not found")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("ENDPOINT_NOT_FOUND")
                .build();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError bindExceptionHandler(BindException e, HttpServletRequest request) {
        Map<String, RestViolation> violations = new HashMap<>();

        for (var error : e.getBindingResult().getFieldErrors()) {
            if (violations.containsKey(error.getField())) {
                violations.get(error.getField()).messages().add(error.getDefaultMessage());
            } else {
                violations.put(
                        error.getField(),
                        new RestViolation(error.getField(), new ArrayList<>(List.of(
                                Optional.ofNullable(error.getDefaultMessage())
                                        .orElse("This field is not valid"))))
                );
            }
        }

        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/validation-error"))
                .title("Validation error")
                .detail("Validation error on request object")
                .instance(URI.create(request.getRequestURI()))
                .code("VALIDATION_ERROR")
                .violations(violations.values().stream().toList())
                .build();
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            ServletRequestBindingException.class,
            AsyncRequestTimeoutException.class,
            ErrorResponseException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class
    })
    default RestError handleException(Exception exception, HttpServletRequest request) {
        if (exception instanceof ConversionNotSupportedException e) {
            return RestError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .type(URI.create("https://problems.greencraze.com.vn/conversion-not-supported"))
                    .title("Conversion not supported")
                    .detail(e.getMessage())
                    .instance(URI.create(request.getRequestURI()))
                    .code("CONVERSION_NOT_SUPPORTED")
                    .build();
        } else if (exception instanceof TypeMismatchException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.greencraze.com.vn/type-mismatch-error"))
                    .title("Type mismatch error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.getRequestURI()))
                    .code("TYPE_MISMATCH_ERROR")
                    .build();
        } else if (exception instanceof HttpMessageNotReadableException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.greencraze.com.vn/http-message-not-readable-error"))
                    .title("HTTP message not readable error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.getRequestURI()))
                    .code("HTTP_MESSAGE_NOT_READABLE_ERROR")
                    .build();
        } else if (exception instanceof HttpMessageNotWritableException e) {
            return RestError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .type(URI.create("https://problems.greencraze.com.vn/http-message-not-writable-error"))
                    .title("HTTP message not writable error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.getRequestURI()))
                    .code("HTTP_MESSAGE_NOT_WRITABLE_ERROR")
                    .build();
        } else if (exception instanceof ErrorResponse e) {
            return RestError.builder()
                    .status(e.getStatusCode().value())
                    .type(URI.create("https://problems.greencraze.com.vn/common-request-error"))
                    .title(e.getBody().getTitle())
                    .detail(e.getBody().getDetail())
                    .instance(URI.create(request.getRequestURI()))
                    .code(((HttpStatus) e.getStatusCode()).name())
                    .build();
        } else {
            throw new IllegalStateException("Unexpected value: " + exception);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError handleGlobalException(Exception e, HttpServletRequest request) {
        log.error("Stack trace of internal server error", e);
        return RestError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .type(URI.create("https://problems.greencraze.com.vn/internal-server-error"))
                .title("Internal server error")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("INTERNAL_SERVER_ERROR")
                .build();
    }

}
