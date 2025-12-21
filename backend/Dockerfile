# 1. 자바 실행 환경(JDK)을 가져옵니다.
FROM openjdk:17-jdk-slim

# 2. 빌드 결과물인 jar 파일의 위치를 변수로 지정합니다.
ARG JAR_FILE=build/libs/*.jar

# 3. jar 파일을 컨테이너 내부로 복사하고 이름을 app.jar로 바꿉니다.
COPY ${JAR_FILE} app.jar

# 4. 컨테이너가 시작될 때 실행할 명령어를 설정합니다.
ENTRYPOINT ["java", "-jar", "/app.jar"]