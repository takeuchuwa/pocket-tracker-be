FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY ./pom.xml /app/.
COPY ./service-discovery /app/discovery/.
RUN mvn -f /app/discovery/pom.xml clean install -Dmaven.test.skip=true spring-boot:repackage -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/discovery/target/*.jar /app/discovery/*.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/discovery/*.jar"]