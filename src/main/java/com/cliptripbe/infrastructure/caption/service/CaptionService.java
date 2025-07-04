package com.cliptripbe.infrastructure.caption.service;

import com.cliptripbe.infrastructure.caption.dto.CaptionRequest;
import com.cliptripbe.infrastructure.caption.dto.CaptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaptionService {

    @Qualifier("captionWebClient")
    private final WebClient captionsWebClient;

    public CaptionResponse getCaptions(CaptionRequest request) {
        long start = System.currentTimeMillis();
        return captionsWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/captions")
                .queryParam("video_url", request.youtubeUrl())
                .queryParam("langs", request.langs())
                .build())
            .retrieve()
            .bodyToMono(CaptionResponse.class)
            .doOnSuccess(place -> {
                long elapsed = System.currentTimeMillis() - start;
                log.info("자막 추출 레이턴시: {} ms", elapsed);
            })
            .block();
    }
}
