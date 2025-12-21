package com.example.backend.post.entity;

import com.example.backend.common.entity.BaseTimeEntity; // 상속받을 공통 클래스
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 접근 제어
public class Post extends BaseTimeEntity { // 1. 상속 추가

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Lob // PostgreSQL에서는 보통 TEXT 타입으로 매핑됩니다.
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 60)
    private String author;

    // 2. createdAt, updatedAt 필드 삭제 (BaseTimeEntity에서 제공)
    // 3. @PrePersist, @PreUpdate 메서드 삭제 (AuditingEntityListener가 처리)

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostAttachment> attachments = new ArrayList<>();

    // 생성자
    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    // 편의 메서드 (기존 유지)
    public void addAttachment(PostAttachment a) {
        attachments.add(a);
        a.setPost(this);
    }

    public void removeAttachment(PostAttachment a) {
        attachments.remove(a);
        a.setPost(null);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}