package com.cliptripbe.infrastructure.caption.dto;

import lombok.Builder;

@Builder
public record CaptionRequest(
    String youtubeUrl,
    String langs
) {

    public static CaptionRequest of(String youtubeUrl) {
        return CaptionRequest.builder()
            .youtubeUrl(youtubeUrl)
            .langs("ko, en")
            .build();
    }
}
