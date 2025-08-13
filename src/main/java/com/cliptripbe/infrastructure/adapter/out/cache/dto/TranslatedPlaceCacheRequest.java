package com.cliptripbe.infrastructure.adapter.out.cache.dto;


import com.cliptripbe.feature.translate.dto.response.TranslatedPlaceAddress;
import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.util.CacheUtils;
import lombok.Builder;

@Builder
public record TranslatedPlaceCacheRequest(
    String key, // 장소명 + 도로명 + 언어(값의 언어)
    TranslationInfoDto translationInfoDto
) {

    public static TranslatedPlaceCacheRequest of(
        TranslatedPlaceAddress translatedPlaceAddresses,
        Language userLanguage
    ) {
        String key = CacheUtils.createTranslatedPlaceKey(
            translatedPlaceAddresses.placeName(),
            translatedPlaceAddresses.roadAddress(),
            userLanguage);
        return TranslatedPlaceCacheRequest.builder()
            .key(key)
            .translationInfoDto(translatedPlaceAddresses.translationInfoDto())
            .build();
    }
}
