package com.cliptripbe.feature.place.domain.vo;

import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import java.util.Set;

public record PlaceDetailVO(
    Long id,
    String name,
    Set<AccessibilityFeature> accessibilityFeatures
) {
    
}
