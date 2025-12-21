package com.example.backend.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속받는 엔티티들에게 필드를 상속해줍니다.
@EntityListeners(AuditingEntityListener.class) // 자동으로 시간을 기록해주는 리스너입니다.
public abstract class BaseTimeEntity {

    @CreatedDate // 생성 시 자동 기록
    @Column(updatable = false) // 수정 시에는 이 컬럼을 건드리지 않습니다.
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시 자동 기록
    private LocalDateTime updatedAt;
}