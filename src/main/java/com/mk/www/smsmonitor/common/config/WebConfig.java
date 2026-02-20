package com.mk.www.smsmonitor.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Swagger UI가 참조하는 /docs/openapi3.json 경로를 실제 파일 위치와 매핑합니다.
        // 1. 빌드된 JAR 실행 시: classpath:/static/docs/
        // 2. 로컬 개발 시: 프로젝트 루트의 build/api-spec/ (openapi3 task로 생성됨)
        registry.addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/", "file:build/api-spec/");
    }
}
