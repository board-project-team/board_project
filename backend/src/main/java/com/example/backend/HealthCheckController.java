package com.example.backend; // 패키지 경로는 파일 위치에 맞게 확인해주세요

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    // 인그레스의 GET 요청에 무조건 200 OK로 대답합니다.
    @GetMapping("/api/health")
    public String health() {
        return "ok";
    }
}
