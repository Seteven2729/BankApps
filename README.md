# BankApps

A Spring Boot-based banking application with REST APIs.

## Requirements

Before running the application, make sure you have the following installed:

* [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
* [Maven 4.0](https://maven.apache.org/download.cgi)
* [Docker](https://www.docker.com/get-started)
* [Docker Compose](https://docs.docker.com/compose/install/)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) (optional, recommended for development)
* [Postman](https://www.postman.com/downloads/) (to test APIs)

## Build and Run

1. **Build the JAR**:

```bash
mvn clean package
```

2. **Start the application using Docker Compose**:

```bash
docker compose up --build
```

Wait until all containers are fully up and running.

## Testing APIs with Postman

1. Open Postman.
2. Import the collection: `BankApps.postman_collection.json`.
3. Use the requests inside the `docker` folder.
4. All requests in the collection are configured to use **port 8081**.

## Notes

* Integration tests use **Testcontainers**, so Docker must be running.
* Application runs on `http://localhost:8081` when using Docker.

## Contact

For issues or questions, please contact \[[seteven2705@gmail.com](mailto:your-email@example.com)].
