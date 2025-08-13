package com.cliptripbe.feature.translate.dto.response;

import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.translate.dto.request.TranslationInfoWithIndex;
import lombok.Builder;

@Builder
public record TranslationInfoDto(
    String translatedName,
    String translatedRoadAddress
) {

    public static TranslationInfoDto from(TranslationInfoWithIndex translatedInfo) {
        if (translatedInfo == null) {
            return null;
        }

        return TranslationInfoDto.builder()
            .translatedName(translatedInfo.translatedName())
            .translatedRoadAddress(translatedInfo.translatedRoadAddress())
            .build();
    }

    public static TranslationInfoDto fromEntity(PlaceTranslation translation) {
        return TranslationInfoDto.builder()
            .translatedName(translation.getName())
            .translatedRoadAddress(translation.getRoadAddress())
            .build();
    }
}