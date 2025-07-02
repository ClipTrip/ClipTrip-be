package com.cliptripbe.feature.place.api.dto.response;

import com.cliptripbe.feature.place.domain.entity.Place;
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
    Boolean bookmarked
) {

    public static PlaceResponseDto of(Place place, Boolean bookmarked) {
        return PlaceResponseDto.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .roadAddress(place.getAddress().roadAddress())
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType().getDisplayName())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .bookmarked(bookmarked)
            .build();
    }
}
