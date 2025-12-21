import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { fetchPosts } from "../api/postsApi";

export default function PostListPage() {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        fetchPosts()
            .then(setPosts)
            .catch((e) => setError(e.message ?? "로딩 실패"))
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <div style={{ padding: 20 }}>로딩 중...</div>;
    if (error) return <div style={{ padding: 20 }}>에러: {error}</div>;

    return (
        <div style={{ padding: 20 }}>
            <h1>게시글 목록</h1>

            <div style={{ marginBottom: 12 }}>
                <Link to="/posts/new">+ 새 글 작성</Link>
            </div>

            <ul>
                {posts.map((p) => (
                    <li key={p.id}>
                        <Link to={`/posts/${p.id}`}>
                            <strong>{p.title}</strong>
                        </Link>{" "}
                        - {p.author}
                    </li>
                ))}
            </ul>
        </div>
    );
}
