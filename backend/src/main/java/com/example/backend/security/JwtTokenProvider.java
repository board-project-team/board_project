package com.example.backend.security;

import com.example.backend.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties props;
    private final Key key;

    public JwtTokenProvider(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    // AuthService에서 호출하는 이름에 맞춰 수정
    public String createToken(String email, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.getAccessTokenExpMinutes() * 60); //

        return Jwts.builder()
                .subject(email) // 아이디(이메일) 설정
                .claim("role", role) // ✅ 권한 정보 추가 (중요!)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    // 토큰에서 권한 정보를 꺼내는 메서드 추가 (필터에서 사용)
    public String getRole(String token) {
        return parse(token).getPayload().get("role", String.class);
    }

    public String getUsername(String token) {
        return parse(token).getPayload().getSubject();
    }

    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token);
    }
}