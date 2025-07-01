package com.cliptripbe.feature.place.domain.type;

import lombok.Getter;

@Getter
public enum PlaceType {
    TOURIST_ATTRACTION("관광지"),
    LUGGAGE_STORAGE("물품보관소"),
    WELFARE_CENTER("복지시설"),
    ACCOMMODATION("숙박시설"),
    RESTAURANT("음식점"),
    TRANSPORTATION("교통시설"),
    CULTURAL_FACILITY("문화시설"),
    PARK("공원"),
    HOSPITAL("병원"),
    SHOPPING("쇼핑");

    private final String displayName;

    PlaceType(String displayName) {
        this.displayName = displayName;
    }
}