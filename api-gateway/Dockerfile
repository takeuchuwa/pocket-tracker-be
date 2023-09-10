FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY ./pom.xml /app/.
COPY ./api-gateway /app/api-gateway/.
RUN mvn -f /app/api-gateway/pom.xml clean package -Pdocker install -Dmaven.test.skip=true spring-boot:repackage -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/api-gateway/target/*.jar /app/api-gateway/*.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/app/api-gateway/*.jar"]