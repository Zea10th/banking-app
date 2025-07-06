# Banking Application

A simple Spring Boot application for basic banking operations: withdrawals, deposits, and balance checks.

---

## Table of Contents

- [Technologies](#technologies)
- [Installation](#installation)
- [Running the App](#running-the-app)
- [Configuration](#configuration)
- [API](#api)
- [Testing](#testing)

---

## Technologies

- Java 21+
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring OpenAPI
- PostgreSQL
- Gradle
- Docker API and Docker Compose
- JUnit 5 (for testing)
- Mockito (for testing)
- Testcontainers (for integration testing)

---

## Installation

```bash
git clone git@github.com:Zea10th/banking-app.git
cd banking-app
./gradlew build
```

## Running the App

### Local Run

Make sure Docker is running before executing project.

```bash
./gradlew bootRun
```

or

```bash
java -jar build/libs/banking-app-0.0.1-SNAPSHOT.jar
```

## Configuration

All environment variables can be set via `application.yml`.

## API

API documentation available at:

Swagger UI: http://localhost:8080/swagger-ui/index.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

## Testing

```bash
./gradlew test
```

If using Testcontainers:

Make sure Docker is running before executing tests.
