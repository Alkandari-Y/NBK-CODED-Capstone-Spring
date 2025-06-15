FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml ./
COPY common/pom.xml common/

COPY common common

RUN mvn dependency:go-offline -B

RUN mvn clean install -DskipTests -pl recommendation -am

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=build /build/recommendation/target/*.jar app.jar
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "app.jar"]
