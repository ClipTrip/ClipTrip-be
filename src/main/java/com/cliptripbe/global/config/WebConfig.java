package com.cliptripbe.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 아무것도 등록하지 않음 => 정적 리소스 서빙 금지
        registry.setOrder(Integer.MAX_VALUE); // 혹시라도 다른 설정보다 늦게 적용되도록
    }
}
