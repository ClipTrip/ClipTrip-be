package com.cliptripbe.feature.place.api.dto.response;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.domain.type.PlaceCategory;
import com.cliptripbe.feature.place.domain.type.PlaceType;
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

    public static PlaceListResponseDto of(PlaceDto placeDto, PlaceType type) {
        return PlaceListResponseDto.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .category(type.getDisplayName())
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .build();
    }

    public static PlaceListResponseDto from(PlaceDto placeDto) {
        return PlaceListResponseDto.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .category(PlaceType.findByCode(placeDto.categoryCode()).getDisplayName())
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .build();
    }
}
