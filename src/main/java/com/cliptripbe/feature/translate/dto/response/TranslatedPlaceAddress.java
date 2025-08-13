package com.cliptripbe.feature.translate.dto.response;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.util.CacheUtils;
import lombok.Builder;

@Builder
public record TranslatedPlaceAddress(
    String placeName,
    String roadAddress,
    TranslationInfo translationInfo,
    Language language
) {

    public static TranslatedPlaceAddress of(PlaceDto placeDto, TranslationInfo translationInfo,
        Language language) {
        return TranslatedPlaceAddress
            .builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .translationInfo(translationInfo)
            .language(language)
            .build();
    }

    public String getCacheKey() {
        return CacheUtils.createTranslatedPlaceKey(placeName, roadAddress, language);
    }
}
