# Start with an OpenJDK base image
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml first for better caching
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# Copy the source code
COPY src ./src

# Make mvnw executable and install Maven
RUN chmod +x mvnw && \
    ./mvnw clean install -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
