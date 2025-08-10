package com.cliptripbe.feature.place.dto.request;

import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PlaceInfoRequest(

    double latitude,

    double longitude,

    String roadAddress,

    String placeName,

    String phone,

    PlaceType type,

    @NotBlank(message = "kakaoPlaceId는 필수입니다.")
    String kakaoPlaceId
) {

    @JsonIgnore
    public Address getAddress() {
        return new Address(latitude, longitude, roadAddress);
    }
}
