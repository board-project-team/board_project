FROM amazoncorretto:17-alpine-jdk

# 1. 환경 변수 설정 (Spring Boot는 대문자와 언더바(_) 형식을 자동으로 매핑합니다)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/board
ENV SPRING_DATASOURCE_USERNAME=board_user
ENV SPRING_DATASOURCE_PASSWORD=board_pw

ENV STORAGE_S3_ENDPOINT=http://minio:9000
ENV STORAGE_S3_ACCESS_KEY=minioadmin
ENV STORAGE_S3_SECRET_KEY=minioadmin123

# 2. ✅ 추가된 기능(소셜 로그인 & JWT)을 위한 환경 변수
# GKE 배포 시 실제 값으로 덮어씌워질 기본값들입니다.
ENV GH_CLIENT_ID=your_github_id
ENV GH_CLIENT_SECRET=your_github_secret

ENV NAVER_CLIENT_ID=your_naver_id
ENV NAVER_CLIENT_SECRET=your_naver_secret

ENV JWT_SECRET=CHANGE_ME_TO_LONG_RANDOM_SECRET_32_CHARS

# 욕설 필터링 API URL 설정
ENV FILTER_API_URL=https://profanity-filter-xxxx.a.run.app/filter

# 3. 기존 빌드 결과물 복사 로직 유지
# (주의: 이 방식은 도커 빌드 전 반드시 './gradlew build'가 선행되어야 합니다)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]