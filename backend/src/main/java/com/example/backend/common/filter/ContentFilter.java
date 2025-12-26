// 필터링 기능을 정의한 인터페이스
// 나중에 파이썬이 아니라 다른 서비스로 바꿔도 Service 코드는 수정할 필요가 없게 됩니다.
public interface ContentFilter {
    void checkProfanity(String content);
}