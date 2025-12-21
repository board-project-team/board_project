package com.example.backend.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_attachments")
@Getter
@Setter
public class PostAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false, unique = true)
    private String storedKey; // MinIO object key (UUID 기반)

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected PostAttachment() {}

    public PostAttachment(
            Post post,
            String originalFilename,
            String storedKey,
            Long size,
            String contentType
    ) {
        this.post = post;
        this.originalFilename = originalFilename;
        this.storedKey = storedKey;
        this.size = size;
        this.contentType = contentType;
        this.createdAt = LocalDateTime.now();
    }

    // getters
}
