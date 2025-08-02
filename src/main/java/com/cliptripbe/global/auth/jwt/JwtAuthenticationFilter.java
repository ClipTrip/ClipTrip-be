package com.cliptripbe.global.auth.jwt;

import static com.cliptripbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.cliptripbe.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.cliptripbe.global.auth.jwt.component.JwtTokenProvider;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    @Value("${cookie.secure}")
    private boolean secureCookie;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest servletRequest,
        @NonNull HttpServletResponse servletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            authenticateWithAccessToken(servletRequest);
        } catch (CustomException e) {
            if (e.getErrorType() == ErrorType.EXPIRED_ACCESS_TOKEN) {
                try {
                    attemptTokenRefresh(servletRequest, servletResponse);
                } catch (CustomException refreshException) {
                    handleException(servletResponse, refreshException);
                    return;
                }
            } else {
                handleException(servletResponse, e);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void attemptTokenRefresh(
        HttpServletRequest servletRequest,
        HttpServletResponse servletResponse
    ) {
        String refreshToken = jwtTokenProvider.extractTokenFromCookies(servletRequest,
            REFRESH_TOKEN);
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            // 재발급
            Authentication authentication = jwtTokenProvider.getAuthenticationFromRefreshToken(
                refreshToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 새 accessToken 재발급 & 쿠키로 설정
            String newAccessToken = jwtTokenProvider.generateToken(authentication).getAccessToken();

            Cookie newAccessTokenCookie = new Cookie(ACCESS_TOKEN.getName(), newAccessToken);
            newAccessTokenCookie.setHttpOnly(true);
            newAccessTokenCookie.setPath("/");
            newAccessTokenCookie.setMaxAge(ACCESS_TOKEN.getValidTime().intValue() / 1000);
            newAccessTokenCookie.setAttribute("SameSite", "Strict");
            newAccessTokenCookie.setSecure(secureCookie);
            servletResponse.addCookie(newAccessTokenCookie);
        }
    }

    private void authenticateWithAccessToken(HttpServletRequest servletRequest) {
        String accessToken = jwtTokenProvider.extractTokenFromCookies(servletRequest, ACCESS_TOKEN);
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }


    private void handleException(HttpServletResponse response, CustomException e)
        throws IOException {
        if (e != null) {
            response.setContentType("application/json");
            response.setStatus(400);
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = new ObjectMapper().writeValueAsString(
                ApiResponse.error(e.getErrorType()));
            response.getWriter().write(jsonResponse);  // ApiResponse의 내용을 JSON으로 변환하여 작성
        } else {
            response.getWriter().write("{\"error\": \"An unexpected error occurred.\"}");
        }
    }
}