package com.cliptripbe.feature.video.api.dto.response;

import com.cliptripbe.feature.video.domain.Video;
import lombok.Builder;

@Builder
public record VideoResponseDto(
    Long videoId,
    String url,
    String summaryKo
) {

    public static VideoResponseDto from(Video video) {
        return VideoResponseDto.builder()
            .videoId(video.getId())
            .url(video.getUrl())
            .summaryKo(video.getSummaryKo())
            .build();
    }
}
