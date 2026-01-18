FROM eclipse-temurin:17-jdk-alpine-3.23
WORKDIR /app
COPY target/gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "app.jar"]