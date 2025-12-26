package com.example.backend.common.dto;
import java.util.List;

public record FilterResponse(
        String status,
        String message,
        List<String> detected_words
) {}