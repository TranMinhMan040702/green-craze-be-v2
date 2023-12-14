package vn.com.greencraze.product.config.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.com.greencraze.commons.advice.CommonControllerAdvice;
import vn.com.greencraze.commons.advice.RestError;
import vn.com.greencraze.product.exception.SaleActiveException;
import vn.com.greencraze.product.exception.SaleDateException;
import vn.com.greencraze.product.exception.SaleExpiredException;
import vn.com.greencraze.product.exception.SaleInactiveException;

import java.net.URI;

@RestControllerAdvice
public class AppControllerAdvice implements CommonControllerAdvice {

    @ExceptionHandler(SaleDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError saleDateExceptionHandler(SaleDateException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/sale-date"))
                .title("Sale Date")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("SALE_DATE")
                .build();
    }

    @ExceptionHandler(SaleActiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError saleActiveExceptionHandler(SaleActiveException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/sale-active"))
                .title("Sale Active")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("SALE_ACTIVE")
                .build();
    }

    @ExceptionHandler(SaleInactiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError saleInactiveExceptionHandler(SaleInactiveException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/sale-inactive"))
                .title("Sale Inactive")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("SALE_INACTIVE")
                .build();
    }

    @ExceptionHandler(SaleExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestError saleExpiredExceptionHandler(SaleExpiredException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.greencraze.com.vn/sale-expired"))
                .title("Sale Expired")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("SALE_EXPIRED")
                .build();
    }

}
