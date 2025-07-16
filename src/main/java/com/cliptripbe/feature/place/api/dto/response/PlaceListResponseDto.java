package com.cliptripbe.feature.place.api.dto.response;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlaceListResponseDto(
    Long placeId,
    String placeName,
    String roadAddress,
    String phone,
    PlaceType type,
    double longitude,
    double latitude,
    Integer placeOrder
) {

    public static PlaceListResponseDto ofDto(PlaceDto placeDto, PlaceType type) {
        return PlaceListResponseDto.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .type(type)
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .build();
    }

    public static PlaceListResponseDto fromDto(PlaceDto placeDto) {
        return PlaceListResponseDto.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .type(PlaceType.findByCode(placeDto.categoryCode()))
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .build();
    }

    public static PlaceListResponseDto fromEntity(Place place, Integer placeOrder) {
        return PlaceListResponseDto.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .roadAddress(place.getAddress().roadAddress())
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .placeOrder(placeOrder)
            .build();
    }

    public static PlaceListResponseDto of(
        Place place,
        PlaceTranslation placeTranslation,
        Integer placeOrder
    ) {
        String placeName = placeTranslation != null && placeTranslation.getName() != null
            ? placeTranslation.getName()
            : place.getName();

        String roadAddress = placeTranslation != null && placeTranslation.getRoadAddress() != null
            ? placeTranslation.getRoadAddress()
            : place.getAddress().roadAddress();

        return PlaceListResponseDto.builder()
            .placeId(place.getId())
            .placeName(placeName)
            .roadAddress(roadAddress)
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .placeOrder(placeOrder)
            .build();
    }
}
