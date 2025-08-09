package com.cliptripbe.feature.user.dto.request;

import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.AgeGroup;
import com.cliptripbe.feature.user.domain.type.CountryCode;
import com.cliptripbe.feature.user.domain.type.Gender;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.user.domain.type.Role;

public record UserSignUpRequest(
    String email,
    String password,
    Gender gender,
    AgeGroup ageGroup,
    Language language,
    CountryCode countryCode
) {

    public User toEntity(String encodePassword) {
        return User.builder()
            .email(email)
            .password(encodePassword)
            .gender(gender)
            .ageGroup(ageGroup)
            .country(countryCode)
            .language(language)
            .role(Role.USER)
            .build();
    }
}
