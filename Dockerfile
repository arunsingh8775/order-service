# Dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app

# If you build with Gradle, the jar is in build/libs/
# (If you use Maven, change to: COPY target/*.jar /app/app.jar)
COPY build/libs/*.jar /app/app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar","--server.port=8081"]