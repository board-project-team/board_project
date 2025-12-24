@RestController
public class HealthCheckController {
    // 인그레스의 GET 요청에 무조건 200 OK로 대답합니다.
    @GetMapping("/api/health")
    public String health() {
        return "ok";
    }
}
