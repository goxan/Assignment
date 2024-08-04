# Assignment Project

This project is a Spring Boot application that uses Kafka and PostgreSQL. It includes a scheduled task that generates messages and sends them to a Kafka topic then it saves it to a PostgreSQL database.

## Prerequisites

- Docker
- Docker Compose

## Used Technologies

- **Java 17**: The primary programming language used for the application.
- **Spring Boot**: Framework for building the application.
- **Gradle**: Build automation tool used for managing dependencies and building the project.
- **Kafka**: Distributed streaming platform used for building real-time data pipelines and streaming applications.
- **PostgreSQL**: Relational database used for storing application data.
- **Liquibase**: Database schema change management tool.
- **Docker**: Containerization platform used for packaging the application and its dependencies.
- **Docker Compose**: Tool for defining and running multi-container Docker applications.
- **Lombok**: Java library used to reduce boilerplate code.
- **JUnit**: Testing framework used for unit testing the application.

## Development Setup

1. Clone the repository:
    ```sh
    git clone git@github.com:goxan/Assignment.git
    cd Assignment
    ```

2. Change application.yml file in src/main/resources folder with your Kafka and PostgreSQL configurations.

3. Build the project:
    ```sh
    ./gradlew build
    ```

4. Run the application:
    ```sh
    ./gradlew bootRun
    ```

## Running with Docker

1. Build the Docker image:
    ```sh
    docker build -t my-app .
    ```

2. Start the services with Docker Compose:
    ```sh
    docker-compose up
    ```

3. Access the application at `http://localhost:8080`.

## Checking Logs

To check the logs of an application running in Docker Compose, follow these steps:  
1. Navigate to the directory where your docker-compose.yml file is located.  
2. Use the docker-compose logs command to view the logs. You can specify the service name to view logs for a specific service or omit it to view logs for all services. 
### Viewing Logs for All Services
```sh
docker-compose logs