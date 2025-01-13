# Use a base image with OpenJDK 17 (or the Java version you're using)
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file built by Spring Boot to the container
COPY target/kniznica-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the app will run on
EXPOSE 9911

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
