FROM eclipse-temurin:17-jdk-alpine-3.23
WORKDIR /app
COPY target/training-type-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]