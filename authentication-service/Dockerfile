FROM eclipse-temurin:17-jdk-alpine-3.23
WORKDIR /app
COPY target/authentication-service-0.0.1-SNAPSHOT.jar app.jar
COPY private_key.pem private_key.pem
COPY public_key.pem public_key.pem
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]