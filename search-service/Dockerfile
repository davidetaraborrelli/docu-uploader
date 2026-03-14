FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY common ./common
COPY search-service ./search-service
COPY user-service/pom.xml ./user-service/pom.xml
COPY document-service/pom.xml ./document-service/pom.xml
COPY notification-service/pom.xml ./notification-service/pom.xml
RUN mvn -pl common,search-service -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/search-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
