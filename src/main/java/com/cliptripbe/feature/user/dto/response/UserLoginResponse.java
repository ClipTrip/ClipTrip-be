package com.cliptripbe.feature.user.dto.response;

import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.auth.jwt.entity.JwtToken;

public record UserLoginResponse(
    JwtToken jwtToken,
    Language language
) {

    public static UserLoginResponse of(JwtToken tokens, Language language) {
        return new UserLoginResponse(
            tokens,
            language
        );
    }
}
