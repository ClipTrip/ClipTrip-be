package com.cliptripbe.feature.video.api.dto.request;

import com.cliptripbe.feature.video.domain.Video;
import com.cliptripbe.infrastructure.caption.utils.CaptionUtils;

public record ExtractPlaceRequestDto(
    String youtubeUrl
) {

    public Video toVideo(String summaryKo, String summaryTranslated) {
        if (summaryTranslated != null) {
            return Video.builder()
                .url(youtubeUrl)
                .youtubeVideoId(CaptionUtils.extractVideoId(youtubeUrl))
                .summaryKo(summaryKo)
                .summaryTranslated(summaryTranslated)
                .translatedLang("ENGLISH")
                .build();
        }
        return Video.builder()
            .url(youtubeUrl)
            .youtubeVideoId(CaptionUtils.extractVideoId(youtubeUrl))
            .summaryKo(summaryKo)
            .build();
    }
}