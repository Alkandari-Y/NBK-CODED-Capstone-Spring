# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /project
COPY . .
RUN mvn clean install -DskipTests -pl notification -am

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /project/notification/target/notification-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8003
ENTRYPOINT ["java", "-jar", "app.jar"]
