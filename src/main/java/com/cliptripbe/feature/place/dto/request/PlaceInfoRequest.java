package com.cliptripbe.feature.place.dto.request;

import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Pattern;

public record PlaceInfoRequest(
    double latitude,
    double longitude,
    String roadAddress,
    String placeName,
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 000-0000-0000 형식이어야 합니다.")
    String phoneNumber,
    PlaceType type,
    String kakaoPlaceId
) {

    @JsonIgnore
    public Address getAddress() {
        return new Address(latitude, longitude, roadAddress);
    }
}
