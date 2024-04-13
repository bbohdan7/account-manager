FROM eclipse-temurin:18-jdk-alpine

ARG JAR_FILE=target/account-manager.jar

WORKDIR /app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
