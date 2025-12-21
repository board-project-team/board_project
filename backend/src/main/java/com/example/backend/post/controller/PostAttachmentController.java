package com.example.backend.post.controller;

import com.example.backend.post.dto.AttachmentResponse;
import com.example.backend.post.service.PostAttachmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/attachments")
@CrossOrigin(origins = "http://localhost:5173")
public class PostAttachmentController {

    private final PostAttachmentService attachmentService;

    public PostAttachmentController(PostAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping
    public List<AttachmentResponse> list(@PathVariable Long postId) {
        return attachmentService.listByPostId(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void upload(
            @PathVariable Long postId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws Exception {
        String username = authentication.getName();
        attachmentService.upload(postId, file, username);
    }

}
