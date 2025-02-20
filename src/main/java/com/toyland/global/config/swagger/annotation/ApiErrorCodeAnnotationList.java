package com.toyland.global.config.swagger.annotation;

import com.toyland.global.exception.type.ApiErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 21.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeAnnotationList {

    ApiErrorCode[] value();
}
