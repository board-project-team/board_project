package com.example.backend.comment.controller;

import com.example.backend.comment.dto.CommentResponse;
import com.example.backend.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    // 특정 게시글의 댓글 목록 조회 (트리 구조)
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성 로직도 미리 만들어두면 테스트하기 좋습니다.
    // @PostMapping("/{postId}/comments") ...
}