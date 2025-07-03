# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory at project root
WORKDIR /project

# Copy everything for multi-module build
COPY . .

# Build the banking module
RUN mvn clean install -DskipTests -pl banking -am

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /project/banking/target/banking-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8001
ENTRYPOINT ["java", "-jar", "app.jar"]
