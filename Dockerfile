FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD target/coding-api-0.0.1-SNAPSHOT.jar coding-api.jar
ENTRYPOINT ["java","-jar","/coding-api.jar"]