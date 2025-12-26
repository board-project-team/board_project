package com.example.backend.post.service;

import com.example.backend.post.dto.PostCreateRequest;
import com.example.backend.post.dto.PostResponse;
import com.example.backend.post.dto.PostUpdateRequest;
import com.example.backend.post.entity.Post;
import com.example.backend.post.entity.PostAttachment;
import com.example.backend.post.repository.PostRepository;
import com.example.backend.storage.FileStorageService;
import com.example.backend.common.filter.ContentFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final FileStorageService storageService;
    private final ContentFilter contentFilter;

    public PostService(PostRepository postRepository, FileStorageService storageService, ContentFilter contentFilter) {
        this.postRepository = postRepository;
        this.storageService = storageService;
        this.contentFilter = contentFilter;
    }

    // CRUD
    // Create 수정 (Controller에서 받은 username을 저장)
    public PostResponse create(PostCreateRequest req, String username) {
        // 게시글 저장 전 필터링 수행
        contentFilter.checkProfanity(req.content());
        contentFilter.checkProfanity(req.title());

        // req.author() 대신 인증된 username을 강제로 사용 (보안상 안전)
        Post post = new Post(req.title(), req.content(), username);
        Post saved = postRepository.save(post);
        return toResponse(saved);
    }

    // Read All
    @Transactional(readOnly = true)
    public List<PostResponse> findAll() {
        return postRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Read
    @Transactional(readOnly = true)
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found"));
        return toResponse(post);
    }

    // Update
    // Update 수정 (권한 체크 추가)
    public PostResponse update(Long id, PostUpdateRequest req, String currentUsername) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다."));

        // 💡 권한 검증: 작성자와 로그인 유저가 다른 경우
        if (!post.getAuthor().equals(currentUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 수정할 수 있습니다.");
        }

        post.update(req.title(), req.content());
        return toResponse(post);
    }

    // Delete
    // Delete 수정 (권한 체크 추가)
    public void delete(Long postId, String currentUsername) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다."));

        // 💡 권한 검증
        if (!post.getAuthor().equals(currentUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 삭제할 수 있습니다.");
        }

        List<String> keys = post.getAttachments().stream()
                .map(PostAttachment::getStoredKey)
                .toList();

        postRepository.delete(post);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (String key : keys) {
                    try {
                        storageService.delete(key);
                    } catch (Exception e) {
                        System.err.println("MinIO delete failed. key=" + key + " err=" + e.getMessage());
                    }
                }
            }
        });
    }

    // --- CRUD End ---

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }


}
