import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function SignupPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");
    const navigate = useNavigate();

    async function handleSignup(e) {
        e.preventDefault();
        try {
            const res = await fetch("http://localhost:8080/api/auth/signup", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password, name }),
            });
            if (res.ok) {
                alert("회원가입 성공! 로그인해주세요.");
                navigate("/login");
            }
        } catch {
            alert("회원가입 실패");
        }
    }

    return (
        <div style={{ padding: 20 }}>
            <h2>회원가입</h2>
            <form onSubmit={handleSignup} style={{ display: "grid", gap: 10, maxWidth: 300 }}>
                <input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="이메일" required />
                <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="비밀번호" required />
                <input type="text" value={name} onChange={e => setName(e.target.value)} placeholder="이름" required />
                <button type="submit">가입하기</button>
            </form>
        </div>
    );
}