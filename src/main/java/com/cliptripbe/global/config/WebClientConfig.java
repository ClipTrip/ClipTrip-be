package com.cliptripbe.global.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${youtube.api.base-url:https://video.google.com}")
    private String youtubeBaseUrl;

    @Value("${openai.api.base-url:https://api.openai.com/v1}")
    private String openaiBaseUrl;

    @Value("${kakao.api.base-url:https://dapi.kakao.com")
    private String kakaoBaseUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Getter
    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

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
    @Qualifier("youtubeWebClient")
    public WebClient youtubeWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(youtubeBaseUrl)
            .codecs(clientCodecConfigurer ->
                clientCodecConfigurer
                    .defaultCodecs()
                    .maxInMemorySize(1024 * 1024) // 1MB (기본값은 256KB)
            )
            .filter((request, next) -> {
                log.info("[WebClient] Request: {} {}", request.method(), request.url());
                request.headers().forEach((k, v) -> log.debug("Header {}={}", k, v));

                return next.exchange(request)
                    .doOnNext(response ->
                        log.info("[WebClient] Response: {} {}",
                            response.statusCode().value(), response.headers().asHttpHeaders()));
            })
            .build();
    }

    @Bean
    public WebClient kakaoWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(kakaoBaseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiKey)
            .build();
    }
}
