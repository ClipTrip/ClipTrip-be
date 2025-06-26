package com.cliptripbe.feature.user.api.dto.request;

public record UserSignInRequestDto(
    String email,
    String password
) {

}
