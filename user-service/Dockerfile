FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY common ./common
COPY user-service ./user-service
COPY document-service/pom.xml ./document-service/pom.xml
COPY notification-service/pom.xml ./notification-service/pom.xml
COPY search-service/pom.xml ./search-service/pom.xml
RUN mvn -pl common,user-service -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
