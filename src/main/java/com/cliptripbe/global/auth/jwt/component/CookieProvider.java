package com.cliptripbe.global.auth.jwt.component;

import com.cliptripbe.global.auth.jwt.entity.TokenType;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    public Cookie createTokenCookie(TokenType tokenType, String token) {
        return createCookie(tokenType.getName(), token, tokenType.getValidTime());
    }

    private Cookie createCookie(String name, String value, long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAge);
        // cookie.setSecure(true); // HTTPS에서만 전송 나중에 풀도록 하기
        return cookie;
    }
}