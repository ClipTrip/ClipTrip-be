package com.cliptripbe.feature.auth.dto;

import lombok.Builder;

@Builder
public record TokenVerifyResponse(
    boolean isExpired
) {

    public static TokenVerifyResponse of(boolean expired) {
        return TokenVerifyResponse.builder()
            .isExpired(expired)
            .build();
    }
}
