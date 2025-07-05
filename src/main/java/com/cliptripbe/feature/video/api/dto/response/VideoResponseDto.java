package com.cliptripbe.feature.video.api.dto.response;

import com.cliptripbe.feature.video.domain.Video;
import lombok.Builder;

@Builder
public record VideoResponseDto(
    Long videoId,
    String url,
    String summary
) {

    public static VideoResponseDto from(Video video) {
        String summaryText = video.getSummaryTranslated() != null
            ? video.getSummaryTranslated()
            : video.getSummaryKo();

        return VideoResponseDto.builder()
            .videoId(video.getId())
            .url(video.getUrl())
            .summary(summaryText)
            .build();
    }
}
