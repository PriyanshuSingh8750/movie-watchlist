# Use a base image for Java
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy the built application JAR file into the container
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
