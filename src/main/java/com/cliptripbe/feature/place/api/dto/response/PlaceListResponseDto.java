package com.cliptripbe.feature.place.api.dto.response;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.domain.type.PlaceCategory;
import lombok.Builder;

@Builder
public record PlaceListResponseDto(
    String placeName,
    String roadAddress,
    String phone,
    String category,
    double longitude,
    double latitude
) {

    public static PlaceListResponseDto of(PlaceDto placeDto, PlaceCategory category) {
        return PlaceListResponseDto.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .category(category.getDescription())
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .build();
    }
}
