package com.mk.www.smsmonitor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("SMS Monitor API")
                .version("v1.0.0")
                .description("SMS 문자 메시지를 분석하여 소비 내역을 관리하는 API");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
