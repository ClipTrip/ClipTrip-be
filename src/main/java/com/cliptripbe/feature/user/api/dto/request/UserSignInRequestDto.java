package com.cliptripbe.feature.user.api.dto.request;

public record UserSignInRequestDto(
    String userId,
    String password
) {

}
