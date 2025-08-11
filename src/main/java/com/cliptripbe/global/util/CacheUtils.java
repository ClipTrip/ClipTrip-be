package com.cliptripbe.global.util;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.user.domain.type.Language;

public class CacheUtils {

    public static String createTranslatedPlaceKey(PlaceDto placeDto, Language userLanguage) {
        return String.format("%s %s %s", placeDto.placeName(), placeDto.roadAddress(),
            userLanguage);
    }
}
