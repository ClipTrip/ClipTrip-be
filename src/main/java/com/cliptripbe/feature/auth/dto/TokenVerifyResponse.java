package com.cliptripbe.feature.auth.dto;

import lombok.Builder;

@Builder
public record TokenVerifyResponse(
    boolean isTokenVerified
) {

    public static TokenVerifyResponse of(boolean isTokenVerified) {
        return TokenVerifyResponse.builder()
            .isTokenVerified(isTokenVerified)
            .build();
    }
}
