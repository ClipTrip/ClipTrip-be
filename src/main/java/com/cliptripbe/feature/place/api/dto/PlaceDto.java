package com.cliptripbe.feature.place.api.dto;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.infrastructure.kakao.dto.KakaoMapResponse;
import lombok.Builder;

@Builder
public record PlaceDto(
    String placeName,
    String address,
    String roadAddress,
    String phone,
    double longitude,
    double latitude
) {

    public static PlaceDto from(KakaoMapResponse.Document document) {
        return PlaceDto.builder()
            .placeName(document.place_name())
            .address(document.address_name())
            .roadAddress(document.road_address_name())
            .phone(document.phone())
            .longitude(Double.parseDouble(document.x()))
            .latitude(Double.parseDouble(document.y()))
            .build();
    }
}
