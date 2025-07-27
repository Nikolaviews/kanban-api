# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
# This assumes you've already built your project and have the JAR in the 'target' directory.
# Example: kanban-api-0.0.1-SNAPSHOT.jar
COPY target/kanban-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on (Spring Boot default is 8080)
EXPOSE 8080

# Run the JAR file when the container launches
ENTRYPOINT ["java","-jar","app.jar"]