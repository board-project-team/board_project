FROM amazoncorretto:17-alpine-jdk
# 이후 내용은 동일
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]