# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set environment variables
ENV APP_HOME=/app
WORKDIR $APP_HOME

# Add a non-root user
RUN groupadd -g 10014 spring && useradd -m -u 10014 -g spring spring

# Copy the JAR file into the container
COPY target/*.jar eshop-server.jar

# Change ownership of the application to the new user
RUN chown -R spring:spring $APP_HOME

# Switch to the non-root user
USER 10014

# Expose the application port
EXPOSE 8080

# Run the application as the non-root user
CMD ["java", "-jar", "eshop-server.jar"]
