package com.cliptripbe.feature.place.dto.response;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlaceListResponse(
    Long placeId,
    String placeName,
    String roadAddress,
    String phone,
    PlaceType type,
    double longitude,
    double latitude,
    Integer placeOrder,
    String kakaoPlaceId
) {

    public static PlaceListResponse ofDto(PlaceDto placeDto, PlaceType type) {
        return PlaceListResponse.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .type(type)
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .kakaoPlaceId(placeDto.kakaoPlaceId())
            .build();
    }

    public static PlaceListResponse fromDto(PlaceDto placeDto) {
        return PlaceListResponse.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .type(PlaceType.findByCode(placeDto.categoryCode()))
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .kakaoPlaceId(placeDto.kakaoPlaceId())
            .build();
    }

    public static PlaceListResponse fromEntity(Place place, Integer placeOrder) {
        String kakaoPlaceId = place.getKakaoPlaceId();
        if (kakaoPlaceId == null) {
            kakaoPlaceId = "-1";
        }
        return PlaceListResponse.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .roadAddress(place.getAddress().roadAddress())
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .placeOrder(placeOrder)
            .kakaoPlaceId(kakaoPlaceId)
            .build();
    }

    public static PlaceListResponse of(
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

        return PlaceListResponse.builder()
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

    public static List<PlaceListResponse> fromList(List<Place> places) {
        return places.stream()
            .map(PlaceListResponse::fromPlace)
            .collect(Collectors.toList());
    }

    public static PlaceListResponse fromPlace(Place place) {
        return PlaceListResponse.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .latitude(place.getAddress().latitude())
            .longitude(place.getAddress().longitude())
            .roadAddress(place.getAddress().roadAddress())
            .build();
    }
}
