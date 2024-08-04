# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper and project files to the container
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src


EXPOSE 8080
ENTRYPOINT ./gradlew bootRun



# Copy the application JAR file to the container
#COPY ./build/libs/app.jar app.jar

# Expose the port the application runs on

#CMD ["./gradlew", ]
# Specify the command to run the application
#CMD ["java", "-jar", "build/libs/app.jar"]