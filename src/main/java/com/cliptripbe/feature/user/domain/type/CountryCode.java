package com.cliptripbe.feature.user.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CountryCode {
    
    US("미국", "United States"),
    CN("중국", "China"),
    JP("일본", "Japan"),
    TW("대만", "Taiwan"),
    HK("홍콩", "Hong Kong"),
    TH("태국", "Thailand"),
    IN("인도", "India"),
    DE("독일", "Germany"),
    FR("프랑스", "France"),
    GB("영국", "United Kingdom"),
    CA("캐나다", "Canada"),
    KR("대한민국", "South Korea"),
    AU("호주", "Australia");

    private final String koName;
    private final String enName;

}