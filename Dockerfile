FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

COPY build/libs/*.jar app.jar

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/app.jar .

ENTRYPOINT ["java", "-jar", "app.jar"]