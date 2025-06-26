package com.cliptripbe.feature.user.api.dto.response;

import com.cliptripbe.feature.user.domain.User;
import lombok.Builder;

@Builder
public record UserInfoResponse(
    Long id,
    String userId
) {

    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
            .id(user.getId())
            .userId(user.getEmail())
            .build();
    }
}
