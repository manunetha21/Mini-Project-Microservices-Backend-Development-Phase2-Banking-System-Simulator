# Use a lightweight JDK 17 image
FROM eclipse-temurin:17-jdk

# Set a working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR into the container
COPY target/*.jar app.jar

# Expose the port your service runs on (8081)
EXPOSE 8081

# Run the Spring Boot JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
