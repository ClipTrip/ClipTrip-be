package com.cliptripbe.feature.user.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Language {
    KOREAN("한국어"),
    ENGLISH("ENGLISH");
    private final String name;
}
