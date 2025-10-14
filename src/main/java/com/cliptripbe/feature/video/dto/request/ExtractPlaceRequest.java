package com.cliptripbe.feature.video.dto.request;

import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.video.domain.entity.Video;
import com.cliptripbe.global.util.YoutubeUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ExtractPlaceRequest(
    @NotBlank
    @Pattern(
        regexp = "(?:https?://)?(?:www\\.)?"
            + "(?:youtube\\.com/(?:watch\\?v=|embed/|shorts/)|youtu\\.be/)"
            + "([A-Za-z0-9_-]{11})"
            + "(?:\\?.*)?",
        message = "유효한 YouTube URL이 아닙니다."
    )
    String youtubeUrl
) {

    public Video toVideo(String summaryKo, String summaryTranslated, Language language) {
        if (summaryTranslated != null) {
            return Video.builder()
                .url(youtubeUrl)
                .youtubeVideoId(YoutubeUtils.extractVideoId(youtubeUrl))
                .summaryKo(summaryKo)
                .summaryTranslated(summaryTranslated)
                .translatedLang(language.getName())
                .build();
        }
        return Video.builder()
            .url(youtubeUrl)
            .youtubeVideoId(YoutubeUtils.extractVideoId(youtubeUrl))
            .summaryKo(summaryKo)
            .build();
    }
}