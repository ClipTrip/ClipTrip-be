package com.cliptripbe.feature.auth.dto;

import com.cliptripbe.feature.user.domain.type.Language;

public record UserLoginResponse(
    Language language,
    String accessToken
) {

    public static UserLoginResponse of(Language language, String accessToken) {
        return new UserLoginResponse(language, accessToken);
    }
}
