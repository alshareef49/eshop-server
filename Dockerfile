# Stage 1: Build JAR using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml /app/
RUN mvn -B dependency:go-offline

# Copy the source code and build the application
COPY src /app/src
RUN mvn -B clean package -DskipTests

# Stage 2: Create a minimal image to run the application
FROM openjdk:21

# Set working directory
WORKDIR /app

# Add a non-root user for security
RUN groupadd -g 10014 spring && useradd -m -u 10014 -g spring spring

# ✅ Correctly reference the builder stage
COPY --from=builder /app/target/*.jar /app/eshop-server.jar

# Change ownership of the application to the new user
RUN chown -R spring:spring /app

# Switch to non-root user
USER 10014

# Expose the application port
EXPOSE 3333

# Run the application
CMD ["java", "-jar", "/app/eshop-server.jar"]
