package com.cliptripbe.infrastructure.inital.initaler.mapper;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import com.cliptripbe.infrastructure.inital.type.DefaultData;

public interface PlaceCsvMapper {

    static final AccessibilityFeature[] FEATURE_ENUMS = { // 상수로 변경
        AccessibilityFeature.FREE_PARKING, AccessibilityFeature.PAID_PARKING,
        AccessibilityFeature.ENTRANCE_FOR_DISABLED, AccessibilityFeature.WHEELCHAIR_RENTAL,
        AccessibilityFeature.DISABLED_TOILET, AccessibilityFeature.EXCLUSIVE_PARKING,
        AccessibilityFeature.LARGE_PARKING, AccessibilityFeature.GUIDE_DOG_ALLOWED,
        AccessibilityFeature.BRAILLE_GUIDE, AccessibilityFeature.AUDIO_GUIDE_KR
    };

    Place map(String line);

    DefaultData getDefaultData();

    static double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}