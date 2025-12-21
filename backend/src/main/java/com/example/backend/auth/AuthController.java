package com.example.backend.auth;

import com.example.backend.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String accessToken) {}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.username(), req.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}

//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final JwtTokenProvider tokenProvider;
//
//    public AuthController(JwtTokenProvider tokenProvider) {
//        this.tokenProvider = tokenProvider;
//    }
//
//    public record LoginRequest(String username, String password) {}
//    public record LoginResponse(String accessToken) {}
//
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
//        // ✅ 연습용: 아무 username/password나 통과
//        // (다음 단계에서 User 테이블/BCrypt로 바꿀 수 있음)
//        String token = tokenProvider.createAccessToken(req.username());
//        return ResponseEntity.ok(new LoginResponse(token));
//    }
//}
