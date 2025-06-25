package com.cliptripbe.feature.place.domain.vo;

import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import java.util.List;

public record PlaceDetailVO(
    Long id,
    String name,
    List<AccessibilityFeature> accessibilityFeatures
) {

    public PlaceDetailVO(Long id, String name, List<AccessibilityFeature> accessibilityFeatures) {
        this.id = id;
        this.name = name;
        this.accessibilityFeatures = accessibilityFeatures;
    }
}
