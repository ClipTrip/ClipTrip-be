package com.cliptripbe.feature.place.domain.type;

import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaceType {

    LARGE_MART("MT1", "Large Mart", "대형마트"),
    CONVENIENCE_STORE("CS2", "Convenience Store", "편의점"),
    CHILD_CARE("PS3", "Child Care & Kindergarten", "어린이집, 유치원"),
    SCHOOL("SC4", "School", "학교"),
    ACADEMY("AC5", "Private Academy", "학원"),
    PARKING_LOT("PK6", "Parking Lot", "주차장"),
    GAS_STATION("OL7", "Gas Station", "주유소, 충전소"),
    SUBWAY_STATION("SW8", "Subway Station", "지하철역"),
    BANK("BK9", "Bank", "은행"),
    CULTURAL_FACILITY("CT1", "Cultural Facility", "문화시설"),
    REAL_ESTATE_AGENCY("AG2", "Real Estate Agency", "중개업소"),
    PUBLIC_INSTITUTION("PO3", "Public Institution", "공공기관"),
    TOURIST_ATTRACTION("AT4", "Tourist Attraction", "관광명소"),
    ACCOMMODATION("AD5", "Accommodation", "숙박"),
    RESTAURANT("FD6", "Restaurant", "음식점"),
    CAFE("CE7", "Cafe", "카페"),
    HOSPITAL("HP8", "Hospital", "병원"),
    PHARMACY("PM9", "Pharmacy", "약국"),

    // 기타
    LUGGAGE_STORAGE("LS1", "Luggage Storage", "물품보관소"),
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