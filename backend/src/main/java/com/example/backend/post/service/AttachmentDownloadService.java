package com.example.backend.post.service;

import com.example.backend.config.S3Properties;
import com.example.backend.post.dto.PresignedUrlResponse;
import com.example.backend.post.entity.PostAttachment;
import com.example.backend.post.repository.PostAttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;

@Service
public class AttachmentDownloadService {

    private final PostAttachmentRepository attachmentRepository;
    private final S3Presigner presigner;
    private final S3Properties props;

    public AttachmentDownloadService(
            PostAttachmentRepository attachmentRepository,
            S3Presigner presigner,
            S3Properties props
    ) {
        this.attachmentRepository = attachmentRepository;
        this.presigner = presigner;
        this.props = props;
    }

    @Transactional(readOnly = true)
    public PresignedUrlResponse createDownloadUrl(Long attachmentId) {
        PostAttachment a = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));

        // URL 유효기간 (실무 기본: 짧게, 예: 60초 ~ 5분)
        Duration ttl = Duration.ofMinutes(2);
        Instant expiresAt = Instant.now().plus(ttl);

        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(a.getStoredKey())
                // 다운로드 시 파일명 보존 (중요!)
                .responseContentDisposition("attachment; filename=\"" + a.getOriginalFilename() + "\"")
                .build();

        GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .getObjectRequest(getReq)
                .build();

        String url = presigner.presignGetObject(presignReq).url().toString();
        return new PresignedUrlResponse(url, expiresAt);
    }
}
