package com.cliptripbe.feature.user.api.dto.request;

import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.user.domain.type.AgeGroup;
import com.cliptripbe.feature.user.domain.type.Gender;
import com.cliptripbe.feature.user.domain.type.Language;

public record UserSignUpRequestDto(
    String email,
    String password,
    Gender gender,
    AgeGroup ageGroup,
    Language language
) {

    public User toEntity(String encodePassword) {
        return User.builder()
            .email(email)
            .password(encodePassword)
            .gender(gender)
            .ageGroup(ageGroup)
            .language(language)
            .build();
    }
}
