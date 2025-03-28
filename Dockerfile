#FROM maven:3.9.1-amazoncorretto-17 AS build
FROM maven:3.9.9-amazoncorretto-21 as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

#FROM eclipse-temurin:17-jdk-alpine
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]