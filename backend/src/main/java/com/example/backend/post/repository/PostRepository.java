package com.example.backend.post.repository;

import com.example.backend.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p left join fetch p.attachments where p.id = :id")
    Optional<Post> findByIdWithAttachments(@Param("id") Long id);
}
