FROM openjdk:21
EXPOSE 3333

# Copy JAR from target directory to the Docker image
ADD target/eshop-server.jar eshop-server.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/eshop-server.jar"]
