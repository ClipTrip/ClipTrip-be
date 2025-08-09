package com.cliptripbe.feature.user.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Locale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
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
    UK("영국", "United Kingdom"),
    CA("캐나다", "Canada"),
    KR("대한민국", "South Korea"),
    AU("호주", "Australia");

    private final String koName;
    private final String enName;

    @JsonCreator
    public static CountryCode from(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("countryCode is required");
        }
        String upper = code.trim().toUpperCase(Locale.ROOT);
        try {
            return CountryCode.valueOf(upper);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported countryCode: " + code);
        }
    }
}