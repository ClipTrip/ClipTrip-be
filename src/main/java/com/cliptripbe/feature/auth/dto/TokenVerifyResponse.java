package com.cliptripbe.feature.auth.dto;

import lombok.Builder;

@Builder
public record TokenVerifyResponse(
    boolean isTokenVerified
) {

    public static TokenVerifyResponse of(boolean expired) {
        return TokenVerifyResponse.builder()
            .isTokenVerified(expired)
            .build();
    }
}
