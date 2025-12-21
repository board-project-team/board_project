package com.example.backend.auth;

import com.example.backend.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;

    // 🔹 연습용 테스트 계정
    private static final Map<String, String> USERS = Map.of(
            "mk", "1234",
            "guest", "1234",
            "admin", "1234"
    );

    public AuthService(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String login(String username, String password) {
        String expected = USERS.get(username);

        if (expected == null || !expected.equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return tokenProvider.createAccessToken(username);
    }
}
