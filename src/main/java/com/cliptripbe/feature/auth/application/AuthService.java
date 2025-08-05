package com.cliptripbe.feature.auth.application;

import static com.cliptripbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.cliptripbe.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.service.UserLoader;
import com.cliptripbe.feature.user.dto.request.UserSignInRequest;
import com.cliptripbe.feature.user.dto.response.UserLoginResponse;
import com.cliptripbe.global.auth.jwt.component.CookieProvider;
import com.cliptripbe.global.auth.jwt.component.JwtTokenProvider;
import com.cliptripbe.global.auth.jwt.entity.JwtToken;
import com.cliptripbe.global.auth.jwt.entity.TokenType;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserLoader userLoader;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;


    public UserLoginResponse userSignIn(
        UserSignInRequest userSignInRequest,
        HttpServletResponse response
    ) {
        createCookieAndAppend(
            userSignInRequest.email(),
            userSignInRequest.password(),
            response
        );

        User user = userLoader.findByEmail(userSignInRequest.email());
        return UserLoginResponse.of(user.getLanguage());
    }

    private void createCookieAndAppend(String userId, String password,
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
            ACCESS_TOKEN, jwtToken.getAccessToken());
        Cookie refreshTokenCookie = cookieProvider.createTokenCookie(
            REFRESH_TOKEN, jwtToken.getRefreshToken());
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    public void logout(HttpServletResponse response) {
        expireCookie(response, ACCESS_TOKEN);
        expireCookie(response, REFRESH_TOKEN);
    }

    private void expireCookie(HttpServletResponse response, TokenType tokenType) {
        Cookie cookie = cookieProvider.createExpireCookie(tokenType);
        response.addCookie(cookie);
    }

    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.extractTokenFromCookies(request, REFRESH_TOKEN);

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorType.EXPIRED_REFRESH_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthenticationFromRefreshToken(
            refreshToken);

        String newAccessToken = jwtTokenProvider.generateToken(authentication).getAccessToken();

        Cookie newAccessTokenCookie = cookieProvider.createTokenCookie(ACCESS_TOKEN,
            newAccessToken);

        response.addCookie(newAccessTokenCookie);
    }
}

