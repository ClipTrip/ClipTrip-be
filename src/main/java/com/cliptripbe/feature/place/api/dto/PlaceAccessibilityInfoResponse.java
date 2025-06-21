package com.cliptripbe.feature.place.api.dto;

import com.cliptripbe.feature.place.domain.AccessibilityFeature;
import com.cliptripbe.feature.place.domain.Place;
import java.util.List;

public record PlaceAccessibilityInfoResponse(
    String name,
    List<AccessibilityFeature> accessibilityFeatures
) {

    public static PlaceAccessibilityInfoResponse from(Place place) {
        return new PlaceAccessibilityInfoResponse(
            place.getName(),
            place.getAccessibilityFeatures()
        );
    }
}
