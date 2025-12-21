import { useState } from "react";
import { useNavigate, Link } from "react-router-dom"; // ✅ Link 추가
import { login } from "../api/auth";

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [err, setErr] = useState("");
    const navigate = useNavigate();

    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";
    const SOCIAL_AUTH_URL = `${API_BASE_URL}/oauth2/authorization`;

    async function handleLogin(e) {
        e.preventDefault();
        setErr("");
        if (!email || !password) {
            setErr("이메일과 비밀번호를 모두 입력해주세요.");
            return;
        }
        try {
            await login(email, password);
            navigate("/posts");
        } catch {
            setErr("로그인 실패: 이메일 또는 비밀번호를 확인하세요.");
        }
    }

    async function quickLogin(e, p) {
        setErr("");
        try {
            await login(e, p);
            navigate("/posts");
        } catch {
            setErr("테스트 계정 로그인 실패");
        }
    }

    return (
        <div style={containerStyle}>
            <div style={cardStyle}>
                <h2>로그인</h2>

                <form onSubmit={handleLogin} style={formStyle}>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="이메일 (email@example.com)"
                        style={inputStyle}
                    />
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="비밀번호"
                        style={inputStyle}
                    />
                    <button type="submit" style={loginButtonStyle}>로그인</button>
                    {err && <div style={errorStyle}>{err}</div>}
                </form>

                <div style={dividerStyle}>또는</div>

                <div style={socialContainerStyle}>
                    <a href={`${SOCIAL_AUTH_URL}/github`} style={{...socialButtonStyle, backgroundColor: "#333", color: "white"}}>
                        GitHub으로 시작하기
                    </a>
                    <a href={`${SOCIAL_AUTH_URL}/naver`} style={{...socialButtonStyle, backgroundColor: "#03C75A", color: "white"}}>
                        네이버로 시작하기
                    </a>
                </div>

                <hr style={{ margin: "20px 0", border: "0.5px solid #eee" }} />

                {/* ✅ 회원가입 유도 링크 추가 */}
                <div style={{ marginBottom: "20px", fontSize: "14px" }}>
                    계정이 없으신가요? <Link to="/signup" style={{ color: "#007bff", fontWeight: "bold" }}>회원가입</Link>
                </div>

                <div style={{ display: "flex", gap: 8, justifyContent: "center" }}>
                    <button onClick={() => quickLogin("admin@test.com", "1234")} style={miniButtonStyle}>관리자</button>
                    <button onClick={() => quickLogin("user@test.com", "1234")} style={miniButtonStyle}>일반유저</button>
                </div>
            </div>
        </div>
    );
}

// 간단한 인라인 스타일 정의
const containerStyle = { display: "flex", justifyContent: "center", alignItems: "center", minHeight: "100vh", backgroundColor: "#f5f5f5" };
const cardStyle = { padding: "40px", backgroundColor: "white", borderRadius: "8px", boxShadow: "0 4px 12px rgba(0,0,0,0.1)", width: "100%", maxWidth: "400px", textAlign: "center" };
const formStyle = { display: "grid", gap: "12px", marginTop: "20px" };
const inputStyle = { padding: "12px", borderRadius: "4px", border: "1px solid #ddd", fontSize: "16px" };
const loginButtonStyle = { padding: "12px", backgroundColor: "#007bff", color: "white", border: "none", borderRadius: "4px", cursor: "pointer", fontSize: "16px", fontWeight: "bold" };
const socialContainerStyle = { display: "grid", gap: "10px" };
const socialButtonStyle = { padding: "12px", borderRadius: "4px", textDecoration: "none", fontSize: "14px", fontWeight: "600", textAlign: "center" };
const errorStyle = { color: "red", fontSize: "14px", marginTop: "8px" };
const dividerStyle = { margin: "20px 0", color: "#888", fontSize: "14px" };
const miniButtonStyle = { padding: "6px 12px", fontSize: "12px", cursor: "pointer", backgroundColor: "#eee", border: "1px solid #ddd", borderRadius: "4px" };
