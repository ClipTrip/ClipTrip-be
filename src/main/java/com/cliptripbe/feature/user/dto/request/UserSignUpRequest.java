package com.cliptripbe.feature.user.dto.request;

import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.AgeGroup;
import com.cliptripbe.feature.user.domain.type.CountryCode;
import com.cliptripbe.feature.user.domain.type.Gender;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.user.domain.type.Role;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserSignUpRequest(
    @NotNull String email,
    @NotNull String password,
    @NotNull Gender gender,
    @NotNull @Min(0) @Max(120) Integer age,
    @NotNull Language language,
    @NotNull CountryCode countryCode
) {

    public User toEntity(String encodePassword) {
        return User.builder()
            .email(email)
            .password(encodePassword)
            .gender(gender)
            .ageGroup(AgeGroup.from(age))
            .country(countryCode)
            .language(language)
            .role(Role.USER)
            .build();
    }
}
