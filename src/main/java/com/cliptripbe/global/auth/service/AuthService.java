package com.cliptripbe.global.auth.service;

import com.cliptripbe.global.auth.jwt.component.CookieProvider;
import com.cliptripbe.global.auth.jwt.component.JwtTokenProvider;
import com.cliptripbe.global.auth.jwt.entity.JwtToken;
import com.cliptripbe.global.auth.jwt.entity.TokenType;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;

    public void createCookieAndAppend(String userId, String password,
        HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userId, password);
        JwtToken jwtToken;
        try {
            Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
            jwtToken = jwtTokenProvider.generateToken(authentication);
        } catch (Exception e) {
            throw new CustomException(ErrorType.FAIL_AUTHENTICATION);
        }

        Cookie accessTokenCookie = cookieProvider.createTokenCookie(
            TokenType.ACCESS_TOKEN,
            jwtToken.getAccessToken());
        Cookie refreshTokenCookie = cookieProvider.createTokenCookie(
            TokenType.REFRESH_TOKEN,
            jwtToken.getRefreshToken()
        );
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}

