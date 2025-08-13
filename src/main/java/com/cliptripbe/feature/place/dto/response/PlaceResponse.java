package com.cliptripbe.feature.place.dto.response;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record PlaceResponse(
    Long placeId,
    String placeName,
    String roadAddress,
    String phone,
    PlaceType type,
    double longitude,
    double latitude,
    Set<AccessibilityFeature> accessibilityFeatures,
    String imageUrl,
    List<Long> bookmarkedIdList,
    String kakaoPlaceId
) {

    public static PlaceResponse of(Place place, List<Long> bookmarkedIdList) {
        return PlaceResponse.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .roadAddress(place.getAddress().roadAddress())
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .accessibilityFeatures(place.getAccessibilityFeatures())
            .imageUrl(place.getImageUrl())
            .bookmarkedIdList(bookmarkedIdList)
            .kakaoPlaceId(place.getKakaoPlaceId())
            .build();
    }

    public static PlaceResponse ofTranslation(
        Place place,
        List<Long> bookmarkedIdList,
        PlaceTranslation placeTranslation
    ) {
        return PlaceResponse.builder()
            .placeId(place.getId())
            .placeName(placeTranslation.getName())
            .roadAddress(placeTranslation.getRoadAddress())
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .accessibilityFeatures(place.getAccessibilityFeatures())
            .imageUrl(place.getImageUrl())
            .bookmarkedIdList(bookmarkedIdList)
            .kakaoPlaceId(place.getKakaoPlaceId())
            .build();
    }
}
