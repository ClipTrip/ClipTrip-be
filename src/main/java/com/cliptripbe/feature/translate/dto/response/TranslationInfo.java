package com.cliptripbe.feature.translate.dto.response;

import com.cliptripbe.feature.translate.dto.request.TranslationInfoWithIndex;
import lombok.Builder;

@Builder
public record TranslationInfo(
    String translatedName,
    String translatedRoadAddress
) {

    public static TranslationInfo from(TranslationInfoWithIndex translatedInfo) {
        return TranslationInfo.builder()
            .translatedName(translatedInfo.translatedName())
            .translatedRoadAddress(translatedInfo.translatedRoadAddress())
            .build();
    }
}