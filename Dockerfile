FROM openjdk:21
EXPOSE 3333
ADD target/eshop-server.jar eshop-server.jar
ENTRYPOINT ["java","-jar","/eshop-server.jar"]