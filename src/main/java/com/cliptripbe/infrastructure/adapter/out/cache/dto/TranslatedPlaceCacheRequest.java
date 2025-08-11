package com.cliptripbe.infrastructure.adapter.out.cache.dto;

import static com.cliptripbe.global.util.CacheUtils.createTranslatedPlaceKey;

import com.cliptripbe.feature.place.domain.vo.TranslationInfo;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.user.domain.type.Language;
import lombok.Builder;

@Builder
public record TranslatedPlaceCacheRequest(
    String key, // 장소명 + 도로명 + 언어(값의 언어)
    TranslationInfo translationInfo
) {

    public static TranslatedPlaceCacheRequest of(
        PlaceDto placeDto,
        TranslationInfo translatedInfo,
        Language userLanguage
    ) {
        String key = createTranslatedPlaceKey(placeDto, userLanguage);
        return TranslatedPlaceCacheRequest.builder()
            .key(key)
            .translationInfo(translatedInfo)
            .build();
    }
}
