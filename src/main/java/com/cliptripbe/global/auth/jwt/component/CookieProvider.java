package com.cliptripbe.global.auth.jwt.component;

import static com.cliptripbe.global.constant.Constant.MILLIS_PER_SECOND;

import com.cliptripbe.global.auth.jwt.entity.TokenType;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    @Value("${cookie.secure}")
    private boolean secureCookie;

    public Cookie createTokenCookie(TokenType tokenType, String token) {
        return createCookie(tokenType.getName(), token, tokenType.getValidTime());
    }

    private Cookie createCookie(String name, String value, long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");
        cookie.setMaxAge((int) maxAge / MILLIS_PER_SECOND);
        cookie.setSecure(secureCookie);
        return cookie;
    }

    public Cookie createExpireCookie(TokenType tokenType) {
        return createCookie(tokenType.getName(), null, 0);
    }

}