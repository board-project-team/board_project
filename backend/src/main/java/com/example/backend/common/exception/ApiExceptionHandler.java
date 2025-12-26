package com.example.backend.common.exception;

import com.example.backend.common.exception.ResourceNotFoundException;
import com.example.backend.common.exception.ProfanityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ProfanityException.class)
    public ResponseEntity<?> handleProfanity(ProfanityException e) {
        return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<?> handleForbidden(Exception e) {
        return ResponseEntity.status(403).body(new ErrorResponse("FORBIDDEN"));
    }

    record ErrorResponse(String message) {}
}
