package com.example.backend.post.service;

import com.example.backend.post.entity.PostAttachment;
import com.example.backend.post.repository.PostAttachmentRepository;
import com.example.backend.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class PostAttachmentDeleteService {

    private final PostAttachmentRepository attachmentRepository;
    private final FileStorageService storageService;

    public PostAttachmentDeleteService(
            PostAttachmentRepository attachmentRepository,
            FileStorageService storageService
    ) {
        this.attachmentRepository = attachmentRepository;
        this.storageService = storageService;
    }

    @Transactional
    public void delete(Long attachmentId) {
        PostAttachment a = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));

        String key = a.getStoredKey();

        // 1) DB 먼저 삭제 (트랜잭션)
        attachmentRepository.delete(a);

        // 2) 트랜잭션 커밋 성공 후에 MinIO 삭제
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    storageService.delete(key);
                } catch (Exception e) {
                    // 실무에서는 여기서:
                    // - 재시도 큐(Kafka/Redis/DB outbox)로 넣거나
                    // - "삭제 실패 목록" 테이블에 기록하고 배치로 정리
                    System.err.println("MinIO delete failed for key=" + key + " : " + e.getMessage());
                }
            }
        });
    }
}
