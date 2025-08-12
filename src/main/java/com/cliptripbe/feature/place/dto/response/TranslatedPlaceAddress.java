package com.cliptripbe.feature.place.dto.response;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.translate.dto.response.TranslationInfo;
import com.cliptripbe.feature.user.domain.type.Language;
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
}
