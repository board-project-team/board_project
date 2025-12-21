package com.example.backend.comment.service;

import com.example.backend.comment.dto.CommentResponse;
import com.example.backend.comment.entity.Comment;
import com.example.backend.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository; // commonRepository가 아닌 commentRepository입니다.

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(Long postId) {
        // 1. 모든 댓글을 한 번에 가져옴 (Repository에서 Fetch Join 사용 권장)
        List<Comment> allComments = commentRepository.findByPostId(postId);

        // 2. 빠른 조회를 위해 Map에 담기
        Map<Long, CommentResponse> map = new HashMap<>();
        List<CommentResponse> roots = new ArrayList<>();

        allComments.forEach(c -> {
            CommentResponse dto = CommentResponse.from(c);
            map.put(dto.id(), dto);

            if (c.getParent() == null) {
                roots.add(dto); // 최상위 댓글
            } else {
                // 부모를 찾아 자식 리스트에 추가 (메모리 상에서 연결)
                CommentResponse parentDto = map.get(c.getParent().getId());
                if (parentDto != null) {
                    parentDto.children().add(dto);
                }
            }
        });

        return roots;
    }
}