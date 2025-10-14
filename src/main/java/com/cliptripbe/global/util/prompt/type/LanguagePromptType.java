package com.cliptripbe.global.util.prompt.type;

import static com.cliptripbe.global.util.prompt.type.PromptConstants.BATCH_PLACE_TRANSLATE_EN;
import static com.cliptripbe.global.util.prompt.type.PromptConstants.BATCH_PLACE_TRANSLATE_JA;
import static com.cliptripbe.global.util.prompt.type.PromptConstants.BATCH_PLACE_TRANSLATE_ZH_CN;
import static com.cliptripbe.global.util.prompt.type.PromptConstants.SINGLE_PLACE_TRANSLATE_EN;
import static com.cliptripbe.global.util.prompt.type.PromptConstants.SINGLE_PLACE_TRANSLATE_JA;
import static com.cliptripbe.global.util.prompt.type.PromptConstants.SINGLE_PLACE_TRANSLATE_ZH_CN;

import com.cliptripbe.feature.user.domain.type.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguagePromptType {

    ENGLISH(
        // Single Place Prompt
        SINGLE_PLACE_TRANSLATE_EN,
        // Batch Place Prompt
        BATCH_PLACE_TRANSLATE_EN
    ),

    JAPANESE(
        SINGLE_PLACE_TRANSLATE_JA,
        BATCH_PLACE_TRANSLATE_JA
    ),

    CHINESE_SIMPLIFIED(
        // Single Place Prompt
        SINGLE_PLACE_TRANSLATE_ZH_CN,
        // Batch Place Prompt
        BATCH_PLACE_TRANSLATE_ZH_CN
    );

    private final String singlePlacePrompt;
    private final String batchPrompt;

    public static LanguagePromptType findByLanguage(Language language) {
        return switch (language) {
            case JAPANESE -> JAPANESE;
            case CHINESE -> CHINESE_SIMPLIFIED;
            case ENGLISH -> ENGLISH;
            default -> null; // Returns null for KOREAN or unsupported languages
        };
    }
}