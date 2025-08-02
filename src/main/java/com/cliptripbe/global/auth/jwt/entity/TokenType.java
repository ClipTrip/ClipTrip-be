package com.cliptripbe.global.auth.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS_TOKEN("accessToken", 1_800_000L), // 30분
    REFRESH_TOKEN("refreshToken", 1_209_600_000L); // 2주

    private final String name;
    private final Long validTime;
}
