package com.toyland.global.config.swagger;

import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotation;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotationList;
import com.toyland.global.config.swagger.response.ErrorResponseDto;
import com.toyland.global.config.swagger.response.ExampleHolder;
import com.toyland.global.exception.type.ApiErrorCode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {


    @Bean
    public OpenAPI OpenApi() {

        Info info = new Info()
            .title("Toyland API")
            .description("API docs")
            .version("1.0.0");

        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name(HttpHeaders.AUTHORIZATION);

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("JWT", securityScheme))
            .addSecurityItem(securityRequirement)
            .info(info);
    }

    /**
     * @return
     */
    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodeAnnotationList apiErrorCodeAnnotationList = handlerMethod.getMethodAnnotation(
                ApiErrorCodeAnnotationList.class);

            // @apiErrorCodeAnnotationList 어노테이션이 붙어있다면
            if (apiErrorCodeAnnotationList != null) {
                generateErrorCodeResponseExample(operation, apiErrorCodeAnnotationList.value());
            } else {
                ApiErrorCodeAnnotation apiErrorCodeAnnotation = handlerMethod.getMethodAnnotation(
                    ApiErrorCodeAnnotation.class);

                // @ApiErrorCodeAnnotationList 어노테이션이 붙어있지 않고
                // @ApiErrorCodeAnnotation 어노테이션이 붙어있다면
                if (apiErrorCodeAnnotation != null) {
                    generateErrorCodeResponseExample(operation, apiErrorCodeAnnotation.value());
                }
            }

            return operation;
        };
    }

    // 여러 개의 에러 응답값 추가
    private void generateErrorCodeResponseExample(Operation operation, ApiErrorCode[] errorCodes) {
        ApiResponses responses = operation.getResponses();

        // ExampleHolder(에러 응답값) 객체를 만들고 에러 코드별로 그룹화
        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(errorCodes)
            .map(
                errorCode -> ExampleHolder.builder()
                    .holder(getSwaggerExample(errorCode))
                    .code(errorCode.getHttpStatus())
                    .name(errorCode.name())
                    .build()
            )
            .collect(Collectors.groupingBy(ExampleHolder::getCode));

        // ExampleHolders를 ApiResponses에 추가
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    // 단일 에러 응답값 예시 추가
    private void generateErrorCodeResponseExample(Operation operation, ApiErrorCode errorCode) {
        ApiResponses responses = operation.getResponses();

        // ExampleHolder 객체 생성 및 ApiResponses에 추가
        ExampleHolder exampleHolder = ExampleHolder.builder()
            .holder(getSwaggerExample(errorCode))
            .name(errorCode.name())
            .code(errorCode.getHttpStatus())
            .build();
        addExamplesToResponses(responses, exampleHolder);
    }

    // ErrorResponseDto 형태의 예시 객체 생성
    private Example getSwaggerExample(ApiErrorCode errorCode) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.from(errorCode);
        Example example = new Example();
        example.setValue(errorResponseDto);

        return example;
    }

    // exampleHolder를 ApiResponses에 추가
    private void addExamplesToResponses(ApiResponses responses,
        Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
            (status, v) -> {
                Content content = new Content();
                MediaType mediaType = new MediaType();
                ApiResponse apiResponse = new ApiResponse();

                v.forEach(
                    exampleHolder -> mediaType.addExamples(
                        exampleHolder.getName(),
                        exampleHolder.getHolder()
                    )
                );
                content.addMediaType("application/json", mediaType);
                apiResponse.setContent(content);
                responses.addApiResponse(String.valueOf(status), apiResponse);
            }
        );
    }

    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
        content.addMediaType("application/json", mediaType);
        apiResponse.content(content);
        responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
    }

}
