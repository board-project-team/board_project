import { Routes, Route, Navigate } from "react-router-dom";
import PostListPage from "./pages/PostListPage";
import PostDetailPage from "./pages/PostDetailPage";
import PostCreatePage from "./pages/PostCreatePage";
import PostEditPage from "./pages/PostEditPage";
import LoginPage from "./pages/LoginPage.jsx";

function isLoggedIn() {
    return !!localStorage.getItem("accessToken");
}

// 로그인 필요한 라우트 보호용
function RequireAuth({ children }) {
    return isLoggedIn() ? children : <Navigate to="/login" replace />;
}

export default function App() {
    return (
        <Routes>
            {/* 기본 진입은 로그인으로 */}
            <Route path="/" element={<Navigate to="/login" replace />} />

            <Route path="/login" element={<LoginPage />} />

            {/* 아래부터는 로그인 필요 */}
            <Route
                path="/posts"
                element={
                    <RequireAuth>
                        <PostListPage />
                    </RequireAuth>
                }
            />
            <Route
                path="/posts/new"
                element={
                    <RequireAuth>
                        <PostCreatePage />
                    </RequireAuth>
                }
            />
            <Route
                path="/posts/:id"
                element={
                    <RequireAuth>
                        <PostDetailPage />
                    </RequireAuth>
                }
            />
            <Route
                path="/posts/:id/edit"
                element={
                    <RequireAuth>
                        <PostEditPage />
                    </RequireAuth>
                }
            />

            {/* 없는 경로는 로그인으로 */}
            <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
    );
}
