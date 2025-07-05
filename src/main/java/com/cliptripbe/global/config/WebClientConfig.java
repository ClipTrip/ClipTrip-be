package com.cliptripbe.global.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${openai.api.base-url:https://api.openai.com/v1}")
    private String openaiBaseUrl;

    @Value("${kakao.api.base-url:https://dapi.kakao.com}")
    private String kakaoBaseUrl;

    @Value("${google.api.base-url:https://maps.googleapis.com}")
    private String googleBaseUrl;

    @Value("${captions.service.base-url}")
    private String captionsBaseUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Getter
    @Value("${google.api.key}")
    private String googleApiKey;

    @Bean
    @Qualifier("openAIWebClient")
    public WebClient openAIWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(openaiBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
            .build();
    }

    @Bean
    @Qualifier("kakaoWebClient")
    public WebClient kakaoWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(kakaoBaseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiKey)
            .build();
    }

    @Bean
    @Qualifier("googleWebClient")
    public WebClient googleMapsWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(googleBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Bean
    @Qualifier("captionWebClient")
    public WebClient captionsWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(captionsBaseUrl)
            .build();
    }
}
