package com.cliptripbe.feature.place.domain.type;

import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaceType {

    LUGGAGE_STORAGE(" ", "Luggage Storage", "물품보관소"),
    CULTURAL_FACILITY("CT1", "Cultural Facility", "문화시설"),
    TOURIST_ATTRACTION("AT4", "Tourist Attraction", "관광명소"),

    ACCOMMODATION("AD5", "Accommodation", "숙박"),
    RESTAURANT("FD6", "Restaurant", "음식점"),
    CAFE("CE7", "Cafe", "카페"),
    PARKING_LOT("PK6", "Parking Lot", "주차장"),
    PUBLIC_INSTITUTION("PO3", "Public Institution", "공공기관"),
    TRANSPORTATION("SW8", "Transportation", "교통시설"),
    HOSPITAL("HP8", "Hospital", "병원"),

    ETC("ETC", "Others", "기타");

    private final String code;
    private final String engName;
    private final String korName;

    public static PlaceType findByCode(String code) {
        return Arrays.stream(values())
            .filter(type -> Objects.equals(type.getCode(), code))
            .findFirst()
            .orElse(ETC);
    }
}