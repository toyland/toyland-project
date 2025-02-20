package com.toyland.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
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
}
