package com.cliptripbe.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${youtube.api.base-url:https://video.google.com}")
    private String youtubeBaseUrl;

    @Value("${openai.api.base-url:https://api.openai.com/v1}")
    private String openaiBaseUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    @Qualifier("openAIWebClient")
    public WebClient openAIWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(openaiBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .build();
    }

    @Bean
    @Qualifier("youtubeWebClient")
    public WebClient youtubeWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(youtubeBaseUrl)
            .build();
    }
}
