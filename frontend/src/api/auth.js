const API_BASE = "http://localhost:8080";
const TOKEN_KEY = "accessToken";

export async function login(username, password) {
    const res = await fetch(`${API_BASE}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
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
