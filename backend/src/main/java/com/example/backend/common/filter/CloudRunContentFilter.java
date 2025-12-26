package com.example.backend.common.filter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.backend.common.exception.ProfanityException;
import com.example.backend.common.dto.FilterResponse;

@Component
public class CloudRunContentFilter implements ContentFilter {

    private final RestTemplate restTemplate;
    private final String filterUrl;

    public CloudRunContentFilter(
            @Value("${filter.api.url}") String filterUrl) {
        this.restTemplate = new RestTemplate();
        this.filterUrl = filterUrl;
    }

    @Override
    public void checkProfanity(String content) {
        // 데이터가 비어있으면 검사할 필요가 없으므로 바로 리턴
        if (content == null || content.isBlank()) return;

        try {
            Map<String, String> requestBody = Map.of("content", content);

            ResponseEntity<FilterResponse> response = restTemplate.postForEntity(
                    filterUrl, requestBody, FilterResponse.class);

            FilterResponse result = response.getBody();

            if (result != null && "rejected".equals(result.status())) {
                throw new ProfanityException("비속어가 포함되어 있습니다: " + String.join(", ", result.detected_words()));
            }
        } catch (ProfanityException e) {
            // 위에서 던진 예외는 그대로 다시 던짐
            throw e;
        } catch (Exception e) {
            // 네트워크 오류 등 서버 호출 실패 시 로그만 남기고 통과시킬지,
            // 아니면 에러를 낼지 결정합니다. (여기서는 로그만 남기고 통과 예시)
            System.err.println("필터 서버 호출 중 오류 발생: " + e.getMessage());
        }
    }
}