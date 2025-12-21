import { getToken, logout } from "./auth";

const API_BASE = "http://localhost:8080";

export async function apiFetch(path, options = {}) {
    const token = getToken();

    const headers = {
        ...(options.headers || {}),
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };

    const res = await fetch(`${API_BASE}${path}`, { ...options, headers });

    // 인증 실패(토큰 만료/무효)
    if (res.status === 401) {
        logout();
        throw new Error("UNAUTHORIZED");
    }

    return res;
}

// JSON 응답을 자주 쓰니까 헬퍼도 제공(선택)
export async function apiJson(path, options = {}) {
    const res = await apiFetch(path, options);
    if (!res.ok) throw new Error(`API 실패 (status=${res.status})`);
    return res.json();
}
