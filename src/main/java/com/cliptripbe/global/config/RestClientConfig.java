package com.cliptripbe.global.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;


@Configuration
public class RestClientConfig {

    @Value("${openai.api.base-url:https://api.openai.com/v1}")
    private String openaiBaseUrl;

    @Value("${kakaoMap.api.base-url:https://dapi.kakao.com}")
    private String kakaoMapBaseUrl;

    @Value("${google.api.base-url:https://maps.googleapis.com}")
    private String googleBaseUrl;

    @Value("${captions.service.base-url}")
    private String captionsBaseUrl;

    @Value("${kakaoMobility.api.base-url:https://apis-navi.kakaomobility.com}")
    private String kakaoMobilityBaseUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Getter
    @Value("${google.api.key}")
    private String googleApiKey;

    @Bean
    @Qualifier("openAIRestClient")
    public RestClient openAIRestClient(RestClient.Builder builder) {
        return builder
            .baseUrl(openaiBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
            .build();
    }

    @Bean
    @Qualifier("kakaoMapRestClient")
    public RestClient kakaoMapRestClient(RestClient.Builder builder) {
        return builder
            .baseUrl(kakaoMapBaseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiKey)
            .build();
    }

    @Bean
    @Qualifier("googleMapsRestClient")
    public RestClient googleMapsRestClient(RestClient.Builder builder) {
        return builder
            .baseUrl(googleBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Bean
    @Qualifier("captionsRestClient")
    public RestClient captionsRestClient(RestClient.Builder builder) {
        return builder
            .baseUrl(captionsBaseUrl)
            .build();
    }

    @Bean
    @Qualifier("kakaoMobilityRestClient")
    public RestClient kakaoMobilityRestClient(RestClient.Builder builder) {
        return builder
            .baseUrl(kakaoMobilityBaseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiKey)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

}
