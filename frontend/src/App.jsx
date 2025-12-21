import { Routes, Route, Navigate } from "react-router-dom";
import PostListPage from "./pages/PostListPage";
import PostDetailPage from "./pages/PostDetailPage";
import PostCreatePage from "./pages/PostCreatePage";
import PostEditPage from "./pages/PostEditPage";
import LoginPage from "./pages/LoginPage.jsx";
import SignupPage from "./pages/SignupPage.jsx"; // ✅ 추가
import OAuth2RedirectHandler from "./pages/OAuth2RedirectHandler";

function isLoggedIn() {
    return !!localStorage.getItem("accessToken");
}

function RequireAuth({ children }) {
    return isLoggedIn() ? children : <Navigate to="/login" replace />;
}

export default function App() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="/login" element={<LoginPage />} />

            {/* ✅ 회원가입 경로 추가 (로그인 없이 접근 가능) */}
            <Route path="/signup" element={<SignupPage />} />

            {/* 소셜 로그인 리다이렉트 처리 */}
            <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />

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

            <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
    );
}