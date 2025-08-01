package com.cliptripbe.global.auth.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS_TOKEN("accessToken", 10 * 60 * 1000L), // 10분
    REFRESH_TOKEN("refreshToken", 30 * 60 * 1000L); // 30

    private final String name;
    private final Long validTime;
}
