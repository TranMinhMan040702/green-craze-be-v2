package vn.com.greencraze.gateway.exception;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import vn.com.greencraze.gateway.dto.RestError;

import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private final ObjectMapper objectMapper;

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable exception = this.getError(request);
        return objectMapper.convertValue(handleException(exception, request), new TypeReference<>() {});
    }

    private RestError handleException(Throwable exception, ServerRequest request) {
        if (exception instanceof InvalidRequestException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.greencraze.com.vn/invalid-request"))
                    .title("Invalid Request")
                    .detail(e.getMessage())
                    .instance(URI.create(request.path()))
                    .code("INVALID_REQUEST")
                    .build();
        } else if (exception instanceof TokenValidationException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.greencraze.com.vn/token-validation-error"))
                    .title("Token Validation Error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.path()))
                    .code("TOKEN_VALIDATION_ERROR")
                    .build();
        } else if (exception instanceof ConversionNotSupportedException e) {
            return RestError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .type(URI.create("https://problems.greencraze.com.vn/conversion-not-supported"))
                    .title("Conversion Not Supported")
                    .detail(e.getMessage())
                    .instance(URI.create(request.path()))
                    .code("CONVERSION_NOT_SUPPORTED")
                    .build();
        } else if (exception instanceof TypeMismatchException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.greencraze.com.vn/type-mismatch-error"))
                    .title("Type Mismatch Error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.path()))
                    .code("TYPE_MISMATCH_ERROR")
                    .build();
        } else if (exception instanceof HttpMessageNotReadableException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.greencraze.com.vn/http-message-not-readable-error"))
                    .title("HTTP Message Not Readable Error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.path()))
                    .code("HTTP_MESSAGE_NOT_READABLE_ERROR")
                    .build();
        } else if (exception instanceof HttpMessageNotWritableException e) {
            return RestError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .type(URI.create("https://problems.greencraze.com.vn/http-message-not-writable-error"))
                    .title("HTTP Message Not Writable Error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.path()))
                    .code("HTTP_MESSAGE_NOT_WRITABLE_ERROR")
                    .build();
        } else if (exception instanceof ErrorResponse e) {
            return RestError.builder()
                    .status(e.getStatusCode().value())
                    .type(URI.create("https://problems.greencraze.com.vn/common-request-error"))
                    .title("Common Request Error")
                    .detail("(%s) %s :: %s".formatted(
                            ((HttpStatus) e.getStatusCode()).name(),
                            e.getBody().getTitle(),
                            e.getBody().getDetail()))
                    .instance(URI.create(request.path()))
                    .code("COMMON_REQUEST_ERROR")
                    .build();
        } else if (exception instanceof WebClientResponseException) {
            return RestError.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .type(URI.create("https://problems.greencraze.com.vn/unauthorized-error"))
                    .title("Internal Server Error")
                    .detail(exception.getMessage())
                    .instance(URI.create(request.path()))
                    .code("UNAUTHORIZED_ERROR")
                    .build();
        } else {
            log.error("Stack trace of internal server error", exception);
            return RestError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .type(URI.create("https://problems.greencraze.com.vn/internal-server-error"))
                    .title("Internal Server Error")
                    .detail(exception.getMessage())
                    .instance(URI.create(request.path()))
                    .code("INTERNAL_SERVER_ERROR")
                    .build();
        }
    }

}
