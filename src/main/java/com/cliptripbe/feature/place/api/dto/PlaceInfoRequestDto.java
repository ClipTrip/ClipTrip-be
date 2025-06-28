package com.cliptripbe.feature.place.api.dto;

public record PlaceInfoRequestDto(
    // 추후에 이름만이 아니라, 위도 경도로 찾게 해야할 것 같다.
    String placeName
) {

}
