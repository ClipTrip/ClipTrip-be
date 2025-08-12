package com.cliptripbe.feature.place.dto.response;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import java.util.Set;
import lombok.Builder;

@Builder
public record PlaceResponse(
    Long placeId,
    String placeName,
    String roadAddress,
    String phone,
    PlaceType placeType,
    double longitude,
    double latitude,
    Set<AccessibilityFeature> accessibilityFeatures,
    Boolean bookmarked,
    String imageUrl
) {

    public static PlaceResponse of(Place place, Boolean bookmarked) {
        return PlaceResponse.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .roadAddress(place.getAddress().roadAddress())
            .phone(place.getPhoneNumber())
            .placeType(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .accessibilityFeatures(place.getAccessibilityFeatures())
            .bookmarked(bookmarked)
            .imageUrl(place.getImageUrl())
            .build();
    }

    public static PlaceResponse ofTranslation(
        Place place,
        Boolean bookmarked,
        PlaceTranslation placeTranslation
    ) {
        return PlaceResponse.builder()
            .placeId(place.getId())
            .placeName(placeTranslation.getName())
            .roadAddress(placeTranslation.getRoadAddress())
            .phone(place.getPhoneNumber())
            .placeType(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .accessibilityFeatures(place.getAccessibilityFeatures())
            .bookmarked(bookmarked)
            .imageUrl(place.getImageUrl())
            .build();
    }
}
