package com.cliptripbe.global.auth.jwt.entity;

import lombok.Builder;

@Builder
public record TokenDto(String accessToken, String refreshToken) {

    public static final String ACCESS_TOKEN = "Access";
    public static final String REFRESH_TOKEN = "Refresh";

}