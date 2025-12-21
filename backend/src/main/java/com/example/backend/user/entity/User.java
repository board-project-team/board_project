package com.example.backend.user.entity;

import com.example.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users") // PostgreSQL에서 user는 예약어일 수 있으므로 users 권장
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column // 소셜 사용자는 비밀번호가 없을 수 있으므로 nullable 유지
    private String password;

//    @Column(nullable = false, unique = true)
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    private String provider;   // "github", "google" 등
    private String providerId; // 소셜 서비스의 고유 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String email, String password, String name, String provider, String providerId, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }

    // 소셜 정보가 변경될 수 있으므로 업데이트 메서드 추가
    public User update(String name) {
        this.name = name;
        return this;
    }

    // Security 권한 반환용 메서드
    public String getRoleKey() {
        return this.role.getKey();
    }
}