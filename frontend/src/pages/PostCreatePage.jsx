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
            console.error(err);
            setError("게시글 생성에 실패했습니다. (로그인/토큰 확인)");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div style={{ padding: 20, maxWidth: 720 }}>
            <div style={{ marginBottom: 12 }}>
                <Link to="/">← 목록으로</Link>
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
