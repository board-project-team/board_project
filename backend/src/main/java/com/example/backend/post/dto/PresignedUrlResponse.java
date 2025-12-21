package com.example.backend.post.dto;

import java.time.Instant;

public record PresignedUrlResponse(
        String url,
        Instant expiresAt
) {}
