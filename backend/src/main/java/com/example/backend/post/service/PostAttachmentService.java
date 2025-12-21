package com.example.backend.post.service;

import com.example.backend.common.exception.ResourceNotFoundException;
import com.example.backend.post.dto.AttachmentResponse;
import com.example.backend.post.entity.Post;
import com.example.backend.post.entity.PostAttachment;
import com.example.backend.post.repository.PostAttachmentRepository;
import com.example.backend.post.repository.PostRepository;
import com.example.backend.storage.FileStorageService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostAttachmentService {

    private final PostRepository postRepository;
    private final PostAttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;

    public PostAttachmentService(
            PostRepository postRepository,
            PostAttachmentRepository attachmentRepository,
            FileStorageService fileStorageService
    ) {
        this.postRepository = postRepository;
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public void upload(Long postId, MultipartFile file, String username) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        // ✅ 작성자만 업로드 가능
        if (!post.getAuthor().equals(username)) {
            throw new AccessDeniedException("Not the author");
        }

        // 1) MinIO 업로드
        String storedKey = fileStorageService.upload(postId, file);

        // 2) DB 저장
        PostAttachment attachment = new PostAttachment(
                post,
                file.getOriginalFilename(),
                storedKey,
                file.getSize(),
                file.getContentType()
        );

        attachmentRepository.save(attachment);
    }

    @Transactional(readOnly = true)
    public List<AttachmentResponse> listByPostId(Long postId) {
        return attachmentRepository.findByPostId(postId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AttachmentResponse toResponse(PostAttachment a) {
        return new AttachmentResponse(
                a.getId(),
                a.getOriginalFilename(),
                a.getContentType(),
                a.getSize(),
                a.getCreatedAt()
        );
    }
}
