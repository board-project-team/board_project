package com.example.backend.post.controller;

import com.example.backend.post.dto.PresignedUrlResponse;
import com.example.backend.post.service.AttachmentDownloadService;
import com.example.backend.post.service.PostAttachmentDeleteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attachments")
@CrossOrigin(origins = "http://localhost:5173")
public class AttachmentDownloadController {

    private final AttachmentDownloadService downloadService;
    private final PostAttachmentDeleteService deleteService;

    public AttachmentDownloadController(AttachmentDownloadService downloadService,  PostAttachmentDeleteService deleteService) {
        this.downloadService = downloadService;
        this.deleteService = deleteService;
    }

    @GetMapping("/{attachmentId}/download-url")
    public PresignedUrlResponse downloadUrl(@PathVariable Long attachmentId) {
        return downloadService.createDownloadUrl(attachmentId);
    }

    @DeleteMapping("/{attachmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long attachmentId) {
        deleteService.delete(attachmentId);
    }
}
