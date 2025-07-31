# Start with an OpenJDK base image
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean install -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
