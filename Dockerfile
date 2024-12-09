FROM maven:3.8.6-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/kata-bank-1.0.0.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
