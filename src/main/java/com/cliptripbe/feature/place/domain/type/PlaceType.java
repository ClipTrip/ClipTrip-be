package com.cliptripbe.feature.place.domain.type;

import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaceType {
    LUGGAGE_STORAGE(" ", "물품보관소"),
    CULTURAL_FACILITY("CT1", "문화시설"),
    TOURIST_ATTRACTION("AT4", "관광명소"),

    // 기타 카테고리
    ACCOMMODATION("AD5", "숙박"),
    RESTAURANT("FD6", "음식점"),
    CAFE("CE7", "카페"),
    PARKING_LOT("PK6", "주차장"),
    PUBLIC_INSTITUTION("PO3", "공공기관"),
    TRANSPORTATION("SW8", "교통시설"),
    HOSPITAL("HP8", "병원"),

    ETC("ETC", "기타");

//    TOURIST_ATTRACTION("관광지"),
//    ACCOMMODATION("숙박시설"),
//    RESTAURANT("음식점"),
//    WELFARE_CENTER("복지시설"),
//    PARK("공원"),
//    SHOPPING("쇼핑"),

    private final String code;
    private final String displayName;

    public static PlaceType findByCode(String code) {
        return Arrays.stream(values())
            .filter(type -> Objects.equals(type.getCode(), code))
            .findFirst()
            .orElse(ETC);
    }
}