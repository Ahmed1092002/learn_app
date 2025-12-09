# Learn App - Todo Management API

A secure Spring Boot REST API for managing todos with user authentication using JWT tokens. This application demonstrates modern Spring Boot practices including security, JPA/Hibernate, and PostgreSQL integration.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Authentication](#authentication)
- [Database Schema](#database-schema)
- [Technologies](#technologies)
- [Deployment](#deployment)

## Features

- **User Authentication**: Secure user registration and login with JWT token-based authentication
- **Todo Management**: Create, read, update, and delete todos
- **Pagination & Sorting**: Paginated todo list with customizable sorting
- **Input Validation**: Comprehensive validation on user inputs
- **Security**: Password encryption with bcrypt, JWT token validation
- **Aspect-Oriented Programming**: Cross-cutting concerns using Spring AOP
- **PostgreSQL Database**: Persistent data storage with JPA/Hibernate
- **Global Exception Handling**: Centralized error response management

## Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **PostgreSQL 12+**
- **Git**

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/Ahmed1092002/learn_app.git
cd learn_app
```

### 2. Set Up PostgreSQL

```bash
# Create the database
createdb learn_app_db

# Or using psql
psql -U postgres
CREATE DATABASE learn_app_db;
```

### 3. Install Dependencies

```bash
./mvnw clean install
```

## Configuration

### Environment Variables

The application supports environment variables for configuration. Create a `.env` file or set system variables:

```bash
# Database Configuration
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/learn_app_db
JDBC_DATABASE_USERNAME=postgres
JDBC_DATABASE_PASSWORD=12345

# JWT Configuration
JWT_SECRET=7zIv4geHzNfcq+MlI0ZM8VKNYBif2LdTM5VvMVOxnsvquRUWGS+Y+C3AvLc01HSx/x84Srf8LvlCkRDH0CdIxg==
JWT_EXPIRATION=3600000

# Server Configuration
PORT=8080
```

### application.properties

The default configuration is in `src/main/resources/application.properties`:

```properties
spring.application.name=learn_app
spring.jpa.database=postgresql
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:postgresql://localhost:5432/learn_app_db
spring.datasource.username=postgres
spring.datasource.password=12345
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
server.port=8080
```

## Running the Application

### Development Mode

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Using Docker

```bash
# Build Docker image
docker build -t learn-app:latest .

# Run container
docker run -p 8080:8080 \
  -e JDBC_DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/learn_app_db \
  -e JDBC_DATABASE_USERNAME=postgres \
  -e JDBC_DATABASE_PASSWORD=12345 \
  -e JWT_SECRET=your-secret-key \
  learn-app:latest
```

### Building JAR

```bash
./mvnw clean package
java -jar target/learn_app-0.0.1-SNAPSHOT.jar
```

## Project Structure

```
learn_app/
├── src/main/java/com/example/learn_app/learn_app/
│   ├── LearnAppApplication.java          # Application entry point
│   ├── aspect/                           # AOP aspects for cross-cutting concerns
│   │   └── TodosAspect.java
│   ├── configuration/                    # Spring configuration classes
│   │   ├── GlobalControllerAdvice.java   # Global exception handler
│   │   ├── PasswordEncoderConfig.java    # Password encryption configuration
│   │   └── SecurityConfig.java           # Spring Security configuration
│   ├── Controller/                       # REST controllers
│   │   ├── AuthController.java           # Authentication endpoints
│   │   └── TodosController.java          # Todo management endpoints
│   ├── dto/                              # Data Transfer Objects
│   │   ├── LoginUser.java
│   │   ├── UserDto.java
│   │   ├── UserResponse.java
│   │   ├── TodosDto.java
│   │   ├── UpdateTodoDto.java
│   │   └── ErrorResponse.java
│   ├── entity/                           # JPA entities
│   │   ├── User.java
│   │   └── Todos.java
│   ├── exception/                        # Custom exceptions
│   │   ├── UserException.java
│   │   └── InvalidPasswordException.java
│   ├── provider/                         # Authentication provider
│   │   └── CustomAuthProvider.java
│   ├── repository/                       # JPA repositories
│   │   ├── UserRepository.java
│   │   └── TodosRepository.java
│   ├── service/                          # Business logic
│   │   ├── UserService.java
│   │   ├── TodosService.java
│   │   └── CustomUserDetailsService.java
│   ├── utils/                            # Utility classes
│   │   ├── JwtUtil.java                  # JWT token generation/validation
│   │   └── JwtAuthenticationFilter.java  # JWT filter for security
│   └── Validate/                         # Validation utilities
├── src/main/resources/
│   └── application.properties            # Application configuration
├── Dockerfile                            # Docker configuration
├── pom.xml                               # Maven configuration
└── README.md                             # This file
```

## API Documentation

See [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) for detailed API endpoints, request/response examples, and error handling.

### Quick Summary

**Authentication Endpoints:**

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

**Todo Endpoints:**

- `GET /api/todos/listTodos` - Get all todos for logged-in user
- `GET /api/todos/getById/{id}` - Get a specific todo
- `POST /api/todos/CreateTodos` - Create a new todo
- `PUT /api/todos/updateTodos` - Update an existing todo
- `DELETE /api/todos/deleteTodo/{id}` - Delete a todo
- `GET /api/todos/paginatedTodos` - Get todos with pagination and filtering

## Authentication

The application uses JWT (JSON Web Token) for authentication.

### Flow

1. User registers with email and password
2. User logs in with credentials
3. Server returns JWT token
4. Client includes token in `Authorization: Bearer <token>` header for subsequent requests
5. Server validates token and processes request

### JWT Configuration

- **Expiration**: 1 hour (configurable via `JWT_EXPIRATION`)
- **Secret**: Configured via environment variable `JWT_SECRET`
- **Algorithm**: HS512

## Database Schema

### User Entity

```sql
CREATE TABLE "user" (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Todos Entity

```sql
CREATE TABLE todos (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  completed BOOLEAN DEFAULT FALSE,
  user_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);
```

## Technologies

| Technology          | Version | Purpose                        |
| ------------------- | ------- | ------------------------------ |
| **Spring Boot**     | 3.4.11  | Application framework          |
| **Java**            | 17      | Programming language           |
| **Spring Security** | 6.x     | Authentication & authorization |
| **Spring Data JPA** | -       | Data access layer              |
| **Hibernate**       | -       | ORM framework                  |
| **PostgreSQL**      | 12+     | Database                       |
| **JWT**             | -       | Token-based authentication     |
| **Lombok**          | -       | Reduce boilerplate code        |
| **Maven**           | 3.8+    | Build automation               |

## Deployment

### Deployment to Render

The application is configured to run on Render or similar cloud platforms:

```yaml
# render.yaml
services:
  - type: web
    name: learn-app
    env: docker
    region: oregon
    plan: free
    dockerfilePath: ./Dockerfile
    envVars:
      - key: JDBC_DATABASE_URL
        value: ${DATABASE_URL}
      - key: JWT_SECRET
        scope: run
```

### Environment Setup for Deployment

1. Set up PostgreSQL database
2. Configure environment variables on your hosting platform
3. Deploy using Docker or JAR

## Testing

Run tests with:

```bash
./mvnw test
```

## Development Notes

### Security Considerations

- Passwords are encrypted using bcrypt
- JWT tokens expire after 1 hour
- CORS and CSRF protection configured
- Validation on all inputs

### Logging

SQL logging is enabled (`spring.jpa.show-sql=true`). Disable in production for performance.

### Hot Reload

Spring Boot DevTools is configured for hot reload during development.

## Troubleshooting

### Database Connection Error

Ensure PostgreSQL is running and `JDBC_DATABASE_URL` is correctly configured.

```bash
# Test PostgreSQL connection
psql -U postgres -h localhost -d learn_app_db
```

### JWT Token Expired

If receiving `401 Unauthorized`, the token has expired. Login again to get a new token.

### Port Already in Use

Change the port in `application.properties` or set `PORT` environment variable.

## Contributing

1. Create a new branch for your feature
2. Commit your changes
3. Push to the branch
4. Create a Pull Request

## License

This project is open source and available under the MIT License.

## Support

For issues and questions, please open an issue on GitHub.

---

**Current Branch**: `enhance_security`
**Last Updated**: December 2025
