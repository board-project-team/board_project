package com.example.backend.common.dto;

public record FilterResponse(
        String status,
        String message,
        List<String> detected_words
) {}