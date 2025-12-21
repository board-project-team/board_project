package com.example.backend.post.dto;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateRequest (
    @NotBlank String title,
    @NotBlank String content
) {}
