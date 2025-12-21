package com.example.backend.auth;

import com.example.backend.security.JwtTokenProvider;
import com.example.backend.user.entity.Role;
import com.example.backend.user.entity.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // 암호화 저장 필수
                .name(name)
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(user.getEmail(), user.getRole().getKey());
    }
}