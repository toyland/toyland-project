package com.toyland.global.config.ai;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OpenApiFeignConfig {

    @Value("${openai.api.key}")
    private String openApiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + openApiKey);
            requestTemplate.header("Content-Type", "application/json");
        };
    }

}
