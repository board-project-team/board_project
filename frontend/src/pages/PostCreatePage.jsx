import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { createPost } from "../api/postsApi";

export default function PostCreatePage() {
    const navigate = useNavigate();

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");

        if (!title.trim() || !content.trim()) {
            setError("title/content는 모두 입력해야 합니다.");
            return;
        }

        try {
            setLoading(true);
            const created = await createPost({ title, content });
            navigate(`/posts/${created.id}`);
        } catch (err) {
            // 기존 apiJson이 throw하는 에러 메시지: "API 실패 (status=400)"
            const errorMsg = err.message || "";

            if (errorMsg.includes("status=400")) {
                // 백엔드의 ProfanityException(400) 발생 시
                setError("제목이나 내용에 비속어가 포함되어 있어 등록할 수 없습니다.");
            }
            else if (errorMsg.includes("status=401") || errorMsg.includes("status=403") || errorMsg === "UNAUTHORIZED") {
                // 인증/권한 실패 시
                setError("로그인 세션이 만료되었거나 권한이 없습니다. 다시 로그인해 주세요.");
            }
            else {
                // 그 외 기타 에러
                setError("게시글 저장 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
            }

            console.error("Post Creation Error:", errorMsg);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div style={{ padding: 20, maxWidth: 720 }}>
            <div style={{ marginBottom: 12 }}>
                <Link to="/posts">← 목록으로</Link>
            </div>

            <h1>게시글 작성</h1>
            {error && <div style={{ margin: "12px 0", color: "crimson" }}>{error}</div>}

            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: 12 }}>
                    <label>
                        제목
                        <input
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            style={{ display: "block", width: "100%", padding: 8, marginTop: 6 }}
                            placeholder="제목"
                        />
                    </label>
                </div>

                <div style={{ marginBottom: 12 }}>
                    <label>
                        내용
                        <textarea
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            style={{ display: "block", width: "100%", padding: 8, marginTop: 6, minHeight: 160 }}
                            placeholder="내용"
                        />
                    </label>
                </div>

                <button type="submit" disabled={loading} style={{ padding: "10px 14px" }}>
                    {loading ? "저장 중..." : "저장"}
                </button>
            </form>
        </div>
    );
}
