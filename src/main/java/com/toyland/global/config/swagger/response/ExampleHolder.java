package com.toyland.global.config.swagger.response;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;
import lombok.Getter;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 21.
 */
@Getter
@Builder
public class ExampleHolder {

    private Example holder;
    private String name;
    private int code;
}
