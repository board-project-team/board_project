package com.example.backend.user.repository;

import com.example.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이미 가입된 유저인지 확인하기 위해 이메일로 조회
    // 이메일 대신 공급자(github)와 해당 서비스의 고유 ID로 유저를 찾습니다.
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    // 일반 로그인 및 가입 여부 확인용
    Optional<User> findByEmail(String email);}