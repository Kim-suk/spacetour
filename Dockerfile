# 빌드 스테이지
FROM gradle:8.4.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# 런타임 스테이지
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
