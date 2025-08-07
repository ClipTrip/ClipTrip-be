package com.cliptripbe.feature.place.dto;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.KakaoMapResponse;
import lombok.Builder;

@Builder
public record PlaceDto(
    String kakaoPlaceId,
    String placeName,
    String address,
    String roadAddress,
    String phone,
    String categoryCode,
    double longitude,
    double latitude
) {

    public static PlaceDto from(KakaoMapResponse.Document document) {
        String roadAddress = document.road_address_name();
        if (document.road_address_name() == null || document.road_address_name().isEmpty()) {
            roadAddress = document.address_name();
        }
        return PlaceDto.builder()
            .kakaoPlaceId(document.id())
            .placeName(document.place_name())
            .address(document.address_name())
            .roadAddress(roadAddress)
            .phone(document.phone())
            .categoryCode(document.category_group_code())
            .longitude(Double.parseDouble(document.x()))
            .latitude(Double.parseDouble(document.y()))
            .build();
    }

    public Place toPlace() {
        return Place.builder()
            .name(placeName)
            .phoneNumber(phone)
            .address(Address.builder()
                .latitude(latitude)
                .longitude(longitude)
                .roadAddress(roadAddress)
                .build()
            )
            .placeType(PlaceType.findByCode(categoryCode))
            .build();
    }
}
