# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory at project root so Maven sees pom.xml
WORKDIR /project

# Copy everything to allow reactor build
COPY . .

# Build the authentication module
RUN mvn clean install -DskipTests -pl authentication -am

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /project/authentication/target/authentication-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]
