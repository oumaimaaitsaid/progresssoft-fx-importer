# Use an official lightweight Java 17 (JDK) runtime as the base image
# "alpine" ensures the image is small and has a reduced attack surface
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container to /app
# All subsequent commandes (COPY, RUN, CMD) with be executed from this path
WORKDIR /app

# --- Dependency Layer ---
# Copy ths maven wrapper scripts ans the project file.
# This is done separately from the source code to leverage Docker's layer caching.
# If pom.xml doesn't change, Docker will reuse the downloaded dependencies layer.
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# Download and cache all project dependencies
RUN ./mvnw dependency:go-offline

# --- Source & Build Layer ---
# Copy the actual application source code
Copy src/ src/

# Build the application and create the executable JAR
# -DskipTests=true is used to speed up the build,
# as tests should be run in a separate CI/CD stage, not in the Docker build itself.
RUN ./mvnw package -DskipTests


# Expose port 8080, which is the default port for Spring Boot
Expose 8080

# This executes the Java application from the generated JAR file
CMD ["java", "-jar", "target/*.jar"]