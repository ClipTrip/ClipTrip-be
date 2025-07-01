package com.cliptripbe.feature.place.api.dto.response;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import java.util.List;
import lombok.Builder;

@Builder
public record PlaceAccessibilityInfoResponse(
    String name,
    List<AccessibilityFeature> accessibilityFeatures,
    Boolean bookmarked
) {

    public static PlaceAccessibilityInfoResponse from(Place place) {
        return PlaceAccessibilityInfoResponse.builder()
            .name(place.getName())
            .accessibilityFeatures(place.getAccessibilityFeatures())
            .build();
    }

    public static PlaceAccessibilityInfoResponse of(Place place, boolean bookmarked) {
        return PlaceAccessibilityInfoResponse.builder()
            .name(place.getName())
            .accessibilityFeatures(place.getAccessibilityFeatures())
            .bookmarked(bookmarked)
            .build();
    }
}
