import { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

export default function OAuth2RedirectHandler() {
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        // URL 파라미터에서 token 추출
        const params = new URLSearchParams(location.search);
        const token = params.get("token");

        if (token) {
            localStorage.setItem("accessToken", token);
            navigate("/posts"); // 성공 시 목록으로
        } else {
            alert("로그인에 실패했습니다.");
            navigate("/login");
        }
    }, [location, navigate]);

    return <div>소셜 로그인 처리 중...</div>;
}