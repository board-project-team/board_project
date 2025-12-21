const API_BASE = "http://34.50.5.152";
const TOKEN_KEY = "accessToken";

// 회원가입 함수도 추가해두면 좋습니다.
export async function signup(email, password, name) {
    const res = await fetch(`${API_BASE}/api/auth/signup`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password, name }),
    });
    if (!res.ok) throw new Error("회원가입 실패");
    return true;
}

export async function login(email, password) { // email로 변경
    const res = await fetch(`${API_BASE}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }), // email로 전송
    });

    if (!res.ok) throw new Error(`login failed (status=${res.status})`);

    const data = await res.json(); // { accessToken: "..." }
    localStorage.setItem(TOKEN_KEY, data.accessToken);
    return data.accessToken;
}

export function logout() {
    localStorage.removeItem(TOKEN_KEY);
}

export function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

export function isLoggedIn() {
    return !!getToken();
}
