import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/auth";

export default function LoginPage() {
    const [username, setUsername] = useState("mk");
    const [password, setPassword] = useState("1234");
    const [err, setErr] = useState("");
    const navigate = useNavigate();

    async function handleLogin(e) {
        e.preventDefault();
        setErr("");
        try {
            await login(username, password);
            navigate("/posts");
        } catch (e) {
            setErr(e.message);
        }
    }

    async function quickLogin(u, p) {
        setErr("");
        try {
            await login(u, p);
            navigate("/posts");
        } catch (e) {
            setErr(e.message);
        }
    }

    return (
        <div style={{ padding: 20 }}>
            <h2>Login</h2>

            <form onSubmit={handleLogin} style={{ display: "grid", gap: 8, maxWidth: 320 }}>
                <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="username" />
                <input value={password} onChange={(e) => setPassword(e.target.value)} placeholder="password" type="password" />
                <button type="submit">로그인</button>
                {err && <div style={{ color: "red" }}>{err}</div>}
            </form>

            <hr style={{ margin: "16px 0" }} />
            <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
                <button onClick={() => quickLogin("guest", "1234")}>게스트</button>
                <button onClick={() => quickLogin("admin", "1234")}>관리자</button>
            </div>
        </div>
    );
}
