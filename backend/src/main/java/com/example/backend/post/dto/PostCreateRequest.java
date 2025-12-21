package com.example.backend.post.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCreateRequest(
    @NotBlank String title,
    @NotBlank String content
    // @NotBlank String author
) {}
