package com.cliptripbe.infrastructure.caption.service;

import com.cliptripbe.infrastructure.caption.dto.CaptionRequest;
import com.cliptripbe.infrastructure.caption.dto.CaptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Slf4j
@Component
@RequiredArgsConstructor
public class CaptionService {

    @Qualifier("captionsRestClient")
    private final RestClient captionsRestClient;

    public CaptionResponse getCaptions(CaptionRequest request) {
        long start = System.currentTimeMillis();

        CaptionResponse captionResponse = captionsRestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/captions")
                .queryParam("video_url", request.youtubeUrl())
                .queryParam("langs", request.langs())
                .build())
            .retrieve()
            .body(CaptionResponse.class);

        long elapsed = System.currentTimeMillis() - start;
        log.info("자막 추출 레이턴시: {} ms", elapsed);
        return captionResponse;
    }
}
