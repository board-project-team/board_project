package com.example.backend.user.repository;

import com.example.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이미 가입된 유저인지 확인하기 위해 이메일로 조회
    Optional<User> findByEmail(String email);
}