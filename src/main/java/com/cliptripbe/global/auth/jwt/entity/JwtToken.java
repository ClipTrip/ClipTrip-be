package com.cliptripbe.global.auth.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
