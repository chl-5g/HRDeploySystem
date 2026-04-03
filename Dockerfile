FROM openjdk:14-slim

WORKDIR /app
COPY target/hr-deploy-system.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
