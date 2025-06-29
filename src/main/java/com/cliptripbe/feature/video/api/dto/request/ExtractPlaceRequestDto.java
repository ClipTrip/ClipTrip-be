package com.cliptripbe.feature.video.api.dto.request;

import com.cliptripbe.feature.video.domain.Video;
import com.cliptripbe.infrastructure.youtube.YoutubeUtils;

public record ExtractPlaceRequestDto(
    String youtubeUrl
) {

    public Video toVideo(String title, String summaryKo) {
        return Video.builder()
            .url(youtubeUrl)
            .youtubeVideoId(YoutubeUtils.extractVideoId(youtubeUrl))
            .title(title)
            .summaryKo(summaryKo)
            .build();
    }
}
