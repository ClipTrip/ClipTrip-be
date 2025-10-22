FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

COPY build/libs/*.jar app.jar

FROM openjdk:21-jdk-slim

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

COPY --from=builder /app/app.jar .

ENTRYPOINT ["java", "-jar", "app.jar"]