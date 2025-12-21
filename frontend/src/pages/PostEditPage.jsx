import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { fetchPost, updatePost } from "../api/postsApi";

export default function PostEditPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        (async () => {
            try {
                setError("");
                setLoading(true);
                const data = await fetchPost(id);
                setTitle(data.title ?? "");
                setContent(data.content ?? "");
            } catch (e) {
                console.error(e);
                setError("게시글을 불러오지 못했습니다.");
            } finally {
                setLoading(false);
            }
        })();
    }, [id]);

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");

        if (!title.trim() || !content.trim()) {
            setError("title/content는 모두 입력해야 합니다.");
            return;
        }

        try {
            setSaving(true);
            await updatePost(id, { title, content });
            navigate(`/posts/${id}`);
        } catch (e) {
            console.error(e);
            setError("수정에 실패했습니다. (권한/로그인 확인)");
        } finally {
            setSaving(false);
        }
    }

    if (loading) return <div style={{ padding: 20 }}>로딩 중...</div>;

    return (
        <div style={{ padding: 20, maxWidth: 720 }}>
            <div style={{ marginBottom: 12 }}>
                <Link to={`/posts/${id}`}>← 상세로</Link> <span style={{ margin: "0 6px" }}>|</span>
                <Link to="/">목록</Link>
            </div>

            <h1>게시글 수정</h1>
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
                            style={{ display: "block", width: "100%", padding: 8, marginTop: 6, minHeight: 180 }}
                            placeholder="내용"
                        />
                    </label>
                </div>

                <button type="submit" disabled={saving} style={{ padding: "10px 14px" }}>
                    {saving ? "저장 중..." : "저장"}
                </button>
            </form>
        </div>
    );
}
