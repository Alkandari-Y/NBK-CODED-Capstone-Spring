FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml ./
COPY common/pom.xml common/
COPY banking/pom.xml banking/
COPY authentication/pom.xml authentication/

COPY common common
COPY banking banking
COPY authentication authentication

RUN mvn dependency:go-offline -B

RUN mvn clean install -DskipTests -pl authentication -am

FROM eclipse-temurin:21-alpine
WORKDIR /app
#COPY --from=build /build/authentication/target/*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]
