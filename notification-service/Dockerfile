FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY common ./common
COPY notification-service ./notification-service
COPY user-service/pom.xml ./user-service/pom.xml
COPY document-service/pom.xml ./document-service/pom.xml
COPY search-service/pom.xml ./search-service/pom.xml
RUN mvn -pl common,notification-service -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
