package com.cliptripbe.feature.video.dto.response;

import com.cliptripbe.feature.video.domain.entity.Video;
import lombok.Builder;

@Builder
public record VideoResponse(
    Long videoId,
    String url,
    String summary
) {

    public static VideoResponse from(Video video) {
        String summaryText = video.getSummaryTranslated() != null
            ? video.getSummaryTranslated()
            : video.getSummaryKo();

        return VideoResponse.builder()
            .videoId(video.getId())
            .url(video.getUrl())
            .summary(summaryText)
            .build();
    }
}
