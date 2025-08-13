package com.cliptripbe.global.util;

import com.cliptripbe.feature.user.domain.type.Language;

public class CacheUtils {

    public static String createTranslatedPlaceKey(
        String placeName,
        String roadAddress,
        Language userLanguage) {

        String normalizedName = placeName.trim().replaceAll("\\s+", "");
        String normalizedAddress = roadAddress.trim().replaceAll("\\s+", "");

        return String.format("%s:%s:%s", normalizedName, normalizedAddress, userLanguage.name());
    }
}
