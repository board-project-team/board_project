package com.example.backend.comment.repository;

import com.example.backend.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 1. 단순 조회: 특정 게시글의 모든 댓글을 가져옴
    List<Comment> findByPostId(Long postId);

    // 2. 성능 최적화 버전 (N+1 문제 방지)
    @Query("select c from Comment c " +
            "left join fetch c.parent " +
            "where c.post.id = :postId " +
            "order by c.parent.id asc nulls first, c.createdAt asc")
    List<Comment> findByPostIdWithParent(@Param("postId") Long postId);
}