package com.example.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 비속어 감지는 보통 400 Bad Request 에러로 처리합니다.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProfanityException extends RuntimeException {
    public ProfanityException(String message) {
        super(message);
    }
}