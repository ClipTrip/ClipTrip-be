package com.cliptripbe.global.util.prompt.type;

import com.cliptripbe.feature.user.domain.type.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SummaryType {
    KOREAN(PromptConstants.SUMMARY_CAPTION),
    ENGLISH(PromptConstants.SUMMARY_CAPTION_EN),
    CHINESE(PromptConstants.SUMMARY_CAPTION_ZH_CN),
    JAPANESE(PromptConstants.SUMMARY_CAPTION_JA);
    private final String template;

    public static SummaryType findByLanguage(Language language) {
        return switch (language) {
            case JAPANESE -> JAPANESE;
            case CHINESE -> CHINESE;
            case ENGLISH -> ENGLISH;
            default -> KOREAN; // Returns null for KOREAN or unsupported languages
        };
    }
}
