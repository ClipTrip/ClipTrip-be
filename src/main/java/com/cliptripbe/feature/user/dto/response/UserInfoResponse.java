package com.cliptripbe.feature.user.dto.response;

import com.cliptripbe.feature.user.domain.entity.User;
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
