FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml ./
COPY common/pom.xml common/

COPY common common

RUN mvn dependency:go-offline -B

RUN mvn clean install -DskipTests -pl notification -am

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=build /build/notification/target/*.jar app.jar
EXPOSE 8003
ENTRYPOINT ["java", "-jar", "app.jar"]
