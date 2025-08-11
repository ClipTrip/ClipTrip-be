package com.cliptripbe.feature.place.domain.vo;

import lombok.Builder;

@Builder
public record TranslationInfo(
    String translatedName,
    String translatedRoadAddress
) {

    public static TranslationInfo from(TranslationInfoWithId translatedInfo) {
        return TranslationInfo.builder()
            .translatedName(translatedInfo.translatedName())
            .translatedRoadAddress(translatedInfo.translatedRoadAddress())
            .build();
    }
}