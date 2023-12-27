FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY . .

CMD ["java", "-jar", "/app/build/libs/task-management-0.0.1.jar"]
