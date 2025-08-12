package com.cliptripbe.global.util;

import com.cliptripbe.feature.user.domain.type.Language;

public class CacheUtils {

    public static String createTranslatedPlaceKey(
        String placeName,
        String roadAddress,
        Language userLanguage) {
        return String.format("%s %s %s", placeName, roadAddress, userLanguage);
    }
}
