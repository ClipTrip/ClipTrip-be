package com.cliptripbe.feature.user.dto.request;

public record UserSignInRequest(
    String email,
    String password
) {

}
