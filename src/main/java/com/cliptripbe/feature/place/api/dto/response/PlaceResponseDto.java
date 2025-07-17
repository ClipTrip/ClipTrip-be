package com.cliptripbe.feature.place.api.dto.response;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import java.util.Set;
import lombok.Builder;

@Builder
public record PlaceResponseDto(
    Long placeId,
    String placeName,
    String roadAddress,
    String phone,
    String type,
    double longitude,
    double latitude,
    Set<AccessibilityFeature> accessibilityFeatures,
    Boolean bookmarked,
    String imageUrl
) {

    public static PlaceResponseDto of(Place place, Boolean bookmarked) {
        return PlaceResponseDto.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .roadAddress(place.getAddress().roadAddress())
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType().getKorName())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .accessibilityFeatures(place.getAccessibilityFeatures())
            .bookmarked(bookmarked)
            .imageUrl(place.getImageUrl())
            .build();
    }

    public static PlaceResponseDto of(
        Place place,
        Boolean bookmarked,
        PlaceTranslation placeTranslation
    ) {
        return PlaceResponseDto.builder()
            .placeId(place.getId())
            .placeName(placeTranslation.getName())
            .roadAddress(placeTranslation.getRoadAddress())
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType().getEngName())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .accessibilityFeatures(place.getAccessibilityFeatures())
            .bookmarked(bookmarked)
            .imageUrl(place.getImageUrl())
            .build();
    }
}
