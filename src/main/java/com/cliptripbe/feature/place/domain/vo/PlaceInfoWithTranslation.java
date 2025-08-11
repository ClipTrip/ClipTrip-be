package com.cliptripbe.feature.place.domain.vo;

import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.user.domain.type.Language;
import lombok.Builder;

@Builder
public record PlaceInfoWithTranslation(
    String placeName,
    String roadAddress,
    String phone,
    PlaceType type,
    double longitude,
    double latitude,
    Integer placeOrder,
    String translatedPlaceName,
    String translatedRoadAddress,
    String kakaoPlaceId,
    Language language
) {

    public static PlaceInfoWithTranslation of(PlaceDto placeDto, TranslationInfo translationInfo,
        Language language) {
        return PlaceInfoWithTranslation.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .type(PlaceType.findByCode(placeDto.categoryCode()))
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .translatedPlaceName(translationInfo.translatedName())
            .translatedRoadAddress(translationInfo.translatedRoadAddress())
            .kakaoPlaceId(placeDto.kakaoPlaceId())
            .language(language)
            .build();
    }
}
