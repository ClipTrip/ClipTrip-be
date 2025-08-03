package com.cliptripbe.global.auth.jwt.component;


import static com.cliptripbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.cliptripbe.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.cliptripbe.global.auth.jwt.entity.JwtToken;
import com.cliptripbe.global.auth.jwt.entity.TokenDto;
import com.cliptripbe.global.auth.jwt.entity.TokenType;
import com.cliptripbe.global.auth.security.CustomerDetailsService;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final CustomerDetailsService customerDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
        CustomerDetailsService customerDetailsService) {
        this.customerDetailsService = customerDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(
                Collectors.joining(","));

        TokenDto tokenDto = createAllToken(authentication.getName(), authorities);

        return JwtToken.builder()
            .grantType(authorities)
            .accessToken(tokenDto.accessToken())
            .refreshToken(tokenDto.refreshToken())
            .build();
    }

    private TokenDto createAllToken(String userId, String role) {
        return TokenDto.builder()
            .accessToken(createToken(userId, role, ACCESS_TOKEN))
            .refreshToken(createToken(userId, role, REFRESH_TOKEN))
            .build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 토큰입니다.", e);
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            // 토큰 타입 확인 (예: "type" 클레임에 저장된 값을 확인)
            String tokenType = claims.get("type", String.class);
            if (ACCESS_TOKEN.getName().equals(tokenType)) {
                throw new CustomException(ErrorType.EXPIRED_ACCESS_TOKEN);
            } else if (REFRESH_TOKEN.getName().equals(tokenType)) {
                throw new CustomException(ErrorType.EXPIRED_REFRESH_TOKEN);
            } else {
                log.info("알 수 없는 토큰 타입입니다.");
            }
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않은 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        log.info("Parsed claims: {}", claims);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        UserDetails userDetails = customerDetailsService.loadUserByUsername(
            claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, token,
            userDetails.getAuthorities());
    }

    public Authentication getAuthenticationFromRefreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = customerDetailsService.loadUserByUsername(
            claims.getSubject());

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String extractTokenFromCookies(HttpServletRequest request, TokenType tokenType) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (tokenType.getName().equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String createToken(String email, String authorities, TokenType type) {
        long now = new Date().getTime();
        long validTime = type.getValidTime();
        return Jwts.builder()
            .setSubject(email)
            .claim("auth", authorities)
            .claim("type", type.getName())
            .setExpiration(new Date(now + validTime))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
