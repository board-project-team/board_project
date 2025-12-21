package com.example.backend.post.dto;

import java.time.LocalDateTime;

public record AttachmentResponse(
        Long id,
        String originalFilename,
        String contentType,
        Long size,
        LocalDateTime createdAt
) {}
