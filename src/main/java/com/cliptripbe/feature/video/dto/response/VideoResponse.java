package com.cliptripbe.feature.video.dto.response;

import com.cliptripbe.feature.video.domain.entity.Video;
import com.cliptripbe.global.util.YoutubeUtils;
import lombok.Builder;

@Builder
public record VideoResponse(
    Long videoId,
    String url,
    String thumbnailUrl,
    String summary
) {

    public static VideoResponse from(Video video) {
        String summaryText = video.getSummaryTranslated() != null
            ? video.getSummaryTranslated()
            : video.getSummaryKo();

        return VideoResponse.builder()
            .videoId(video.getId())
            .url(video.getUrl())
            .thumbnailUrl(YoutubeUtils.getThumbnailUrl(video.getYoutubeVideoId()))
            .summary(summaryText)
            .build();
    }
}
