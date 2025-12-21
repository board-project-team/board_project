import { useEffect, useState } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import { fetchPost, deletePost } from "../api/postsApi";
import {
    listAttachments,
    uploadAttachment,
    deleteAttachment,
    getDownloadUrl,
} from "../api/filesApi";

export default function PostDetailPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [post, setPost] = useState(null);
    const [attachments, setAttachments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [selectedFile, setSelectedFile] = useState(null);
    const [uploading, setUploading] = useState(false);

    async function reloadAttachments() {
        const list = await listAttachments(id);
        setAttachments(list);
    }

    useEffect(() => {
        (async () => {
            try {
                setLoading(true);
                const p = await fetchPost(id);
                setPost(p);
                await reloadAttachments();
            } catch (e) {
                setError(e.message ?? "로딩 실패");
            } finally {
                setLoading(false);
            }
        })();
    }, [id]);

    async function onUpload() {
        if (!selectedFile) return alert("파일을 선택하세요");

        try {
            setUploading(true);
            await uploadAttachment(id, selectedFile);
            await reloadAttachments();
            setSelectedFile(null);
            alert("업로드 완료");
        } catch (e) {
            console.error(e);
            alert(e.message ?? "업로드 실패");
        } finally {
            setUploading(false);
        }
    }

    async function onDownload(attachmentId) {
        const data = await getDownloadUrl(attachmentId); // { url }
        window.location.href = data.url;
    }

    async function onDeleteAttachment(attachmentId) {
        const ok = window.confirm("첨부파일을 삭제할까요?");
        if (!ok) return;

        try {
            await deleteAttachment(attachmentId);
            setAttachments((prev) => prev.filter((x) => x.id !== attachmentId));
        } catch (e) {
            console.error(e);
            alert(e.message ?? "삭제 실패");
        }
    }

    async function handleDeletePost() {
        const ok = window.confirm("정말 삭제할까요?");
        if (!ok) return;

        try {
            await deletePost(id);
            navigate("/");
        } catch (e) {
            console.error(e);
            alert(e.message ?? "삭제 실패");
        }
    }

    if (loading) return <div style={{ padding: 20 }}>로딩 중...</div>;
    if (error) return <div style={{ padding: 20 }}>에러: {error}</div>;
    if (!post) return <div style={{ padding: 20 }}>게시글이 없습니다.</div>;

    return (
        <div style={{ padding: 20 }}>
            <div style={{ marginBottom: 12 }}>
                <Link to="/">← 목록으로</Link>
            </div>

            <h1>{post.title}</h1>
            <div style={{ color: "#555", marginBottom: 12 }}>작성자: {post.author}</div>

            <div style={{ whiteSpace: "pre-wrap", lineHeight: 1.6 }}>{post.content}</div>

            <hr style={{ margin: "20px 0" }} />

            <h3>첨부파일</h3>
            <div style={{ marginTop: 12, display: "flex", gap: 8, alignItems: "center" }}>
                <input type="file" onChange={(e) => setSelectedFile(e.target.files?.[0] ?? null)} />
                <button onClick={onUpload} disabled={uploading}>
                    {uploading ? "업로드 중..." : "파일 등록"}
                </button>
            </div>

            {attachments.length === 0 ? (
                <div style={{ color: "#777", marginTop: 12 }}>첨부파일 없음</div>
            ) : (
                <ul style={{ marginTop: 12 }}>
                    {attachments.map((a) => (
                        <li key={a.id} style={{ display: "flex", gap: 8, alignItems: "center" }}>
                            <span>{a.originalFilename}</span>
                            <span style={{ fontSize: 12, color: "#777" }}>({a.size} bytes)</span>
                            <button onClick={() => onDownload(a.id)}>다운로드</button>
                            <button onClick={() => onDeleteAttachment(a.id)}>삭제</button>
                        </li>
                    ))}
                </ul>
            )}

            <hr style={{ margin: "20px 0" }} />

            <div style={{ fontSize: 12, color: "#777" }}>
                createdAt: {post.createdAt}
                <br />
                updatedAt: {post.updatedAt}
            </div>

            <div style={{ marginTop: 16, display: "flex", gap: 8 }}>
                <Link to={`/posts/${id}/edit`}>수정</Link>
                <button onClick={handleDeletePost}>삭제</button>
            </div>
        </div>
    );
}
