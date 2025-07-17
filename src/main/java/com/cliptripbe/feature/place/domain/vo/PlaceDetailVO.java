package com.cliptripbe.feature.place.domain.vo;

import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import java.util.Set;

public record PlaceDetailVO(
    Long id,
    String name,
    Set<AccessibilityFeature> accessibilityFeatures
) {

    public PlaceDetailVO(Long id, String name, Set<AccessibilityFeature> accessibilityFeatures) {
        this.id = id;
        this.name = name;
        this.accessibilityFeatures = accessibilityFeatures;
    }
}
