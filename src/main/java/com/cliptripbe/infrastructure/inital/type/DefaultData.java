package com.cliptripbe.infrastructure.inital.type;

import com.cliptripbe.feature.place.domain.type.PlaceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultData {

    BF_CULTURE_TOURISM(
        "문화 시설 장소",
        PlaceType.CULTURAL_FACILITY,
        "전국",
        "한국문화정보원_전국 배리어프리 문화예술관광지_20221125.csv",
        "문화 시설 장소입니다."
    ),

    STORAGE_SEOUL(
        "물품 보관함(서울)",
        PlaceType.LUGGAGE_STORAGE,
        "서울",
        "RB_TURIST_THNG_DPSTRY_LCINFO_20221231.csv",
        "서울에 있는 물품 보관소입니다."
    ),

    ACCOMMODATION_SEOUL(
        "장애인 관광객을 위한 숙박 북마크(서울)",
        PlaceType.ACCOMMODATION,
        "서울",
        "RB_DSPSN_TURIST_TURSM_INFO_20221231.csv",
        "장애인 관광객을 위한 서울에 속한 숙박 장소입니다."
    ),

    INCHEON_ACCESSIBLE_TOURISM(
        "인천_무장애_여행지",
        PlaceType.CULTURAL_FACILITY,
        "인천",
        "인천_무장애_여행지.csv",
        "장애인도 편하게 숙박할 수 있는 여행지입니다."
    ),

    SOKCHO_OPEN_TOURISM(
        "속초_열린_여행지",
        PlaceType.CULTURAL_FACILITY,
        "속초",
        "속초_열린_여행지.csv",
        "장애인도 편하게 숙박할 수 있는 여행지입니다."
    ),

    BUSAN_ACCESSIBLE_TOURISM(
        "부산_무장애_여행지",
        PlaceType.CULTURAL_FACILITY,
        "부산",
        "부산_무장애_여행지.csv",
        "장애인도 편하게 숙박할 수 있는 여행지입니다."
    );

    private final String name;
    private final PlaceType placeType;
    private final String region;
    private final String fileName;
    private final String description;
}