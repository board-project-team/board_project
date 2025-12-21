import { apiJson, apiFetch } from "./apiFetch";

// 첨부 리스트
export async function listAttachments(postId) {
    return apiJson(`/api/posts/${postId}/attachments`);
}

// 업로드
export async function uploadAttachment(postId, file) {
    const form = new FormData();
    form.append("file", file);

    const res = await apiFetch(`/api/posts/${postId}/attachments`, {
        method: "POST",
        body: form,
        // ✅ FormData는 Content-Type 직접 지정 금지
    });

    if (!res.ok) throw new Error(`Upload failed (status=${res.status})`);
    return true;
}

// presigned URL 발급 → 다운로드
export async function getDownloadUrl(attachmentId) {
    return apiJson(`/api/attachments/${attachmentId}/download-url`);
}

// 첨부 삭제
export async function deleteAttachment(attachmentId) {
    const res = await apiFetch(`/api/attachments/${attachmentId}`, { method: "DELETE" });
    if (!res.ok) throw new Error(`Delete attachment failed (status=${res.status})`);
    return true;
}
