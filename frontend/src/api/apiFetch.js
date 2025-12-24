// apiFetch.js 상단
import {getToken, logout} from "./auth.js";

const API_BASE = import.meta.env.VITE_API_BASE_URL || ""; // 🚩 || "" 를 반드시 추가하세요.
// 환경 변수가 있으면 사용하고, 없으면 로컬 개발용 주소를 기본값으로 사용합니다.

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
    console.log("요청 경로: " + path);
    const res = await apiFetch(path, options);
    if (!res.ok) throw new Error(`API 실패 (status=${res.status})`);
    return res.json();
}
