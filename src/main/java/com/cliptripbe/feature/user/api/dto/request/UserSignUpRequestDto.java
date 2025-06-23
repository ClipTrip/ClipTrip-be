package com.cliptripbe.feature.user.api.dto.request;

import com.cliptripbe.feature.user.domain.User;

public record UserSignUpRequestDto(
    String userId,
    String password
) {

    public User toEntity(String encodePassword) {
        return User.builder()
            .userId(userId)
            .password(encodePassword)
            .build();
    }
}
