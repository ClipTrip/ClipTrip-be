package com.cliptripbe.feature.user.dto.response;

import com.cliptripbe.feature.user.domain.type.Language;

public record UserLoginResponse(
    Language language
) {

    public static UserLoginResponse of(Language language) {
        return new UserLoginResponse(language);
    }
}
