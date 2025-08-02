package com.cliptripbe.feature.user.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Language {
    KOREAN("한국어"),
    ENGLISH("영어"),
    CHINESE("중국어"),
    JAPANESE("일본어");

    private final String name;
}
