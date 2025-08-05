package com.cliptripbe.global.auth.jwt;

import static com.cliptripbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;

import com.cliptripbe.global.auth.jwt.component.JwtTokenProvider;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final List<AntPathRequestMatcher> ALLOWED_URLS = Arrays.asList(
        new AntPathRequestMatcher("/swagger-ui/**"),
        new AntPathRequestMatcher("/swagger-ui.html"),
        new AntPathRequestMatcher("/v3/api-docs/**"),
        new AntPathRequestMatcher("/webjars/**"),
        new AntPathRequestMatcher("/swagger-resources/**"),
        new AntPathRequestMatcher("/api/v1/auth/**")
    );

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest servletRequest,
        @NonNull HttpServletResponse servletResponse,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.extractTokenFromCookies(servletRequest, ACCESS_TOKEN);

        if (isAllowedUrl(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            if (jwtTokenProvider.validateToken(accessToken)) {
                authenticateWithAccessToken(accessToken);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (CustomException e) {
            handleException(servletResponse, e);
        }
    }

    private void authenticateWithAccessToken(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
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

    private boolean isAllowedUrl(HttpServletRequest request) {
        return ALLOWED_URLS.stream().anyMatch(matcher -> matcher.matches(request));
    }
}