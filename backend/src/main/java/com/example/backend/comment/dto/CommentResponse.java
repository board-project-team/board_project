package com.example.backend.comment.dto;

import com.example.backend.comment.entity.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record CommentResponse(
        Long id,
        String content,
        String author,
        List<CommentResponse> children, // 조립을 위해 가변 리스트로 사용
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor(),
                new ArrayList<>(), // 빈 가변 리스트로 초기화
                comment.getCreatedAt()
        );
    }
}