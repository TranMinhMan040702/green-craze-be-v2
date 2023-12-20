package vn.com.greencraze.commons.annotation;

import vn.com.greencraze.commons.enumeration.Microservice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InternalApi {

    Microservice[] value() default {};

}
