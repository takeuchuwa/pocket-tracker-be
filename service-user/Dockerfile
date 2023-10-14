# syntax = edrevo/dockerfile-plus

INCLUDE+ ./Dockerfile.build

FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /app/pom.xml /app/.
COPY --from=deps /app/feign-clients-lib /app/feign-clients-lib/.
COPY --from=deps /app/jwt-validation-lib /app/jwt-validation-lib/.
COPY --from=deps /app/service-user /app/service-user/.
RUN mvn -f /app/service-user/pom.xml clean package -Pdockerdev install -Dmaven.test.skip=true spring-boot:repackage -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/service-user/target/*.jar /app/service-user/*.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/service-user/*.jar"]