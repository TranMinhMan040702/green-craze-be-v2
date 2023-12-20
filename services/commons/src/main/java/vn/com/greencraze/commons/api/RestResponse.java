package vn.com.greencraze.commons.api;

import org.springframework.http.HttpStatus;

public record RestResponse<T>(
        int status,
        T data
) {

    public static <T> RestResponse<T> ok(T data) {
        return new RestResponse<>(HttpStatus.OK.value(), data);
    }

    public static <T> RestResponse<T> created(T data) {
        return new RestResponse<>(HttpStatus.CREATED.value(), data);
    }

}
