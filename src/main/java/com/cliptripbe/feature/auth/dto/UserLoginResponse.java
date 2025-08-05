package com.cliptripbe.feature.auth.dto;

import com.cliptripbe.feature.user.domain.type.Language;

public record UserLoginResponse(
    Language language
) {

    public static UserLoginResponse of(Language language) {
        return new UserLoginResponse(language);
    }
}
