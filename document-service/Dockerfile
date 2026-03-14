FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY common ./common
COPY document-service ./document-service
COPY user-service/pom.xml ./user-service/pom.xml
COPY notification-service/pom.xml ./notification-service/pom.xml
COPY search-service/pom.xml ./search-service/pom.xml
RUN mvn -pl common,document-service -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/document-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
