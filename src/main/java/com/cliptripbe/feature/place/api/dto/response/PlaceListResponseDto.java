package com.cliptripbe.feature.place.api.dto.response;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.domain.type.PlaceCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public static PlaceListResponseDto from(PlaceDto placeDto) {
        return PlaceListResponseDto.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .build();
    }
}
