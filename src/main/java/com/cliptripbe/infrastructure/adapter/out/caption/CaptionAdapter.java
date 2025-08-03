package com.cliptripbe.infrastructure.adapter.out.caption;

import com.cliptripbe.infrastructure.adapter.out.caption.dto.CaptionResponse;
import com.cliptripbe.infrastructure.port.caption.VideoContentExtractPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Slf4j
@Component
@RequiredArgsConstructor
public class CaptionAdapter implements VideoContentExtractPort {

    @Qualifier("captionsRestClient")
    private final RestClient captionsRestClient;

    @Override
    public CaptionResponse getCaptions(String youtubeUrl) {
        long start = System.currentTimeMillis();

        CaptionResponse captionResponse = captionsRestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/captions")
                .queryParam("video_url", youtubeUrl)
                .queryParam("langs", "ko,en")
                .build())
            .retrieve()
            .body(CaptionResponse.class);

        long elapsed = System.currentTimeMillis() - start;
        log.info("자막 추출 레이턴시: {} ms", elapsed);
        return captionResponse;
    }
}
