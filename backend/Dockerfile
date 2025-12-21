FROM amazoncorretto:17-alpine-jdk

# 1. 환경 변수 설정 (Spring Boot는 대문자와 언더바(_) 형식을 자동으로 매핑합니다)
# 예: storage.s3.endpoint -> STORAGE_S3_ENDPOINT
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/board
ENV SPRING_DATASOURCE_USERNAME=board_user
ENV SPRING_DATASOURCE_PASSWORD=board_pw

ENV STORAGE_S3_ENDPOINT=http://minio:9000
ENV STORAGE_S3_ACCESS_KEY=minioadmin
ENV STORAGE_S3_SECRET_KEY=minioadmin123

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]