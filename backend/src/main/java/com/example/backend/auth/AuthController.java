package com.example.backend.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // DTO 정의
    public record LoginRequest(String email, String password) {}
    public record SignupRequest(String email, String password, String name) {}
    public record AuthResponse(String accessToken) {}

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        authService.signup(req.email(), req.password(), req.name());
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.email(), req.password());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}