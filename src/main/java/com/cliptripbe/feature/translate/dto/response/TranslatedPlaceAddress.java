package com.cliptripbe.feature.translate.dto.response;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.util.CacheUtils;
import lombok.Builder;

@Builder
public record TranslatedPlaceAddress(
    String placeName,
    String roadAddress,
    TranslationInfoDto translationInfoDto,
    Language language
) {

    public static TranslatedPlaceAddress of(PlaceDto placeDto, TranslationInfoDto translationInfoDto,
        Language language) {
        return TranslatedPlaceAddress
            .builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .translationInfoDto(translationInfoDto)
            .language(language)
            .build();
    }

    public String getCacheKey() {
        return CacheUtils.createTranslatedPlaceKey(placeName, roadAddress, language);
    }

    public String getTranslationKey() {
        return placeName + roadAddress;
    }
}
