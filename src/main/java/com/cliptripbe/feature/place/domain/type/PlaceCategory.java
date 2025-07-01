package com.cliptripbe.feature.place.domain.type;

import static com.cliptripbe.global.response.type.ErrorType.ENUM_RESOURCE_NOT_FOUND;

import com.cliptripbe.global.response.exception.CustomException;
import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaceCategory {

    CULTURAL_FACILITY("CT1", "문화시설"),
    TOURIST_ATTRACTION("AT4", "관광명소"),

    // 기타 카테고리
    ACCOMMODATION("AD5", "숙박"),
    RESTAURANT("FD6", "음식점"),
    CAFE("CE7", "카페"),
    PARKING_LOT("PK6", "주차장"),
    PUBLIC_INSTITUTION("PO3", "공공기관");

    private final String code;
    private final String description;

    public static PlaceCategory findByCode(String code) {
        return Arrays.stream(values())
            .filter(category -> Objects.equals(category.getCode(), code))
            .findFirst()
            .orElseThrow(() -> new CustomException(ENUM_RESOURCE_NOT_FOUND));
    }
}
