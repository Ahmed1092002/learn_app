# API Documentation

Complete API reference for Learn App Todo Management API.

**Base URL**: `http://localhost:8080/api`

**API Version**: 1.0

## Table of Contents

- [Authentication Endpoints](#authentication-endpoints)
  - [Register User](#register-user)
  - [Login User](#login-user)
- [Todo Endpoints](#todo-endpoints)
  - [Get All Todos](#get-all-todos)
  - [Get Todo by ID](#get-todo-by-id)
  - [Create Todo](#create-todo)
  - [Update Todo](#update-todo)
  - [Delete Todo](#delete-todo)
  - [Get Paginated Todos](#get-paginated-todos)
- [Error Handling](#error-handling)
- [Response Formats](#response-formats)

---

## Authentication Endpoints

### Register User

Register a new user account.

**Endpoint**: `POST /auth/register`

**Request Headers**:

```
Content-Type: application/json
```

**Request Body**:

```json
{
  "email": "user@example.com",
  "password": "SecurePassword123!"
}
```

**Request Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| email | string | Yes | User's email address (must be unique) |
| password | string | Yes | User's password (minimum 8 characters recommended) |

**Success Response** (200 OK):

```json
{
  "id": 1,
  "email": "user@example.com",
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjM3MjU0OTAwLCJleHAiOjE2MzcyNTg1MDB9.abcdef123456",
  "message": "User registered successfully"
}
```

**Error Response** (400 Bad Request):

```json
{
  "timestamp": "2024-12-09T10:30:00",
  "status": 400,
  "error": "Invalid input",
  "message": "Email already exists",
  "path": "/api/auth/register"
}
```

**cURL Example**:

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "SecurePassword123!"
  }'
```

---

### Login User

Authenticate and retrieve JWT token.

**Endpoint**: `POST /auth/login`

**Request Headers**:

```
Content-Type: application/json
```

**Request Body**:

```json
{
  "email": "user@example.com",
  "password": "SecurePassword123!"
}
```

**Request Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| email | string | Yes | User's email address |
| password | string | Yes | User's password |

**Success Response** (200 OK):

```json
{
  "id": 1,
  "email": "user@example.com",
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjM3MjU0OTAwLCJleHAiOjE2MzcyNTg1MDB9.abcdef123456",
  "message": "Login successful"
}
```

**Error Response** (401 Unauthorized):

```json
{
  "timestamp": "2024-12-09T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password",
  "path": "/api/auth/login"
}
```

**cURL Example**:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "SecurePassword123!"
  }'
```

---

## Todo Endpoints

All todo endpoints require JWT authentication. Include the token in the `Authorization` header:

```
Authorization: Bearer <your_jwt_token>
```

### Get All Todos

Retrieve all todos for the authenticated user.

**Endpoint**: `GET /todos/listTodos`

**Request Headers**:

```
Authorization: Bearer <token>
```

**Success Response** (200 OK):

```json
[
  {
    "id": 1,
    "title": "Buy groceries",
    "description": "Milk, eggs, bread",
    "completed": false,
    "userId": 1,
    "createdAt": "2024-12-09T10:00:00",
    "updatedAt": "2024-12-09T10:00:00"
  },
  {
    "id": 2,
    "title": "Complete project",
    "description": "Finish the spring boot application",
    "completed": true,
    "userId": 1,
    "createdAt": "2024-12-08T14:30:00",
    "updatedAt": "2024-12-09T09:15:00"
  }
]
```

**Error Response** (401 Unauthorized):

```json
{
  "timestamp": "2024-12-09T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired token",
  "path": "/api/todos/listTodos"
}
```

**cURL Example**:

```bash
curl -X GET http://localhost:8080/api/todos/listTodos \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

---

### Get Todo by ID

Retrieve a specific todo by its ID.

**Endpoint**: `GET /todos/getById/{id}`

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | Todo ID |

**Request Headers**:

```
Authorization: Bearer <token>
```

**Success Response** (200 OK):

```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "completed": false,
  "userId": 1,
  "createdAt": "2024-12-09T10:00:00",
  "updatedAt": "2024-12-09T10:00:00"
}
```

**Error Response** (404 Not Found):

```json
{
  "timestamp": "2024-12-09T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Todo with ID 999 not found",
  "path": "/api/todos/getById/999"
}
```

**cURL Example**:

```bash
curl -X GET http://localhost:8080/api/todos/getById/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

---

### Create Todo

Create a new todo.

**Endpoint**: `POST /todos/CreateTodos`

**Request Headers**:

```
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body**:

```json
{
  "title": "Learn Spring Boot",
  "description": "Study advanced Spring Security and AOP",
  "completed": false
}
```

**Request Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| title | string | Yes | Todo title (max 255 characters) |
| description | string | No | Todo description |
| completed | boolean | No | Completion status (default: false) |

**Success Response** (200 OK):

```json
{
  "id": 3,
  "title": "Learn Spring Boot",
  "description": "Study advanced Spring Security and AOP",
  "completed": false,
  "userId": 1,
  "createdAt": "2024-12-09T10:35:00",
  "updatedAt": "2024-12-09T10:35:00"
}
```

**Error Response** (400 Bad Request):

```json
{
  "timestamp": "2024-12-09T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Title is required",
  "path": "/api/todos/CreateTodos"
}
```

**cURL Example**:

```bash
curl -X POST http://localhost:8080/api/todos/CreateTodos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Study advanced Spring Security and AOP",
    "completed": false
  }'
```

---

### Update Todo

Update an existing todo.

**Endpoint**: `PUT /todos/updateTodos`

**Request Headers**:

```
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body**:

```json
{
  "id": 1,
  "title": "Buy groceries and cook",
  "description": "Milk, eggs, bread, and prepare dinner",
  "completed": true
}
```

**Request Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | Todo ID to update |
| title | string | No | Updated title |
| description | string | No | Updated description |
| completed | boolean | No | Updated completion status |

**Success Response** (200 OK):

```json
{
  "id": 1,
  "title": "Buy groceries and cook",
  "description": "Milk, eggs, bread, and prepare dinner",
  "completed": true,
  "userId": 1,
  "createdAt": "2024-12-09T10:00:00",
  "updatedAt": "2024-12-09T10:40:00"
}
```

**Error Response** (404 Not Found):

```json
{
  "timestamp": "2024-12-09T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Todo with ID 999 not found",
  "path": "/api/todos/updateTodos"
}
```

**cURL Example**:

```bash
curl -X PUT http://localhost:8080/api/todos/updateTodos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -d '{
    "id": 1,
    "title": "Buy groceries and cook",
    "description": "Milk, eggs, bread, and prepare dinner",
    "completed": true
  }'
```

---

### Delete Todo

Delete a todo by ID.

**Endpoint**: `DELETE /todos/deleteTodo/{id}`

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | Todo ID to delete |

**Request Headers**:

```
Authorization: Bearer <token>
```

**Success Response** (200 OK):

```
"Todo deleted successfully"
```

**Error Response** (404 Not Found):

```json
{
  "timestamp": "2024-12-09T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Todo with ID 999 not found",
  "path": "/api/todos/deleteTodo/999"
}
```

**cURL Example**:

```bash
curl -X DELETE http://localhost:8080/api/todos/deleteTodo/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

---

### Get Paginated Todos

Retrieve todos with pagination, sorting, and filtering.

**Endpoint**: `GET /todos/paginatedTodos`

**Query Parameters**:
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | integer | 1 | Page number (1-indexed) |
| size | integer | 10 | Items per page |
| sortBy | string | id | Sort field (id, title, completed, createdAt, updatedAt) |
| ascending | boolean | true | Sort order (true for ascending, false for descending) |
| title | string | "" | Filter todos by title (partial match) |

**Request Headers**:

```
Authorization: Bearer <token>
```

**Success Response** (200 OK):

```json
{
  "content": [
    {
      "id": 1,
      "title": "Buy groceries",
      "description": "Milk, eggs, bread",
      "completed": false,
      "userId": 1,
      "createdAt": "2024-12-09T10:00:00",
      "updatedAt": "2024-12-09T10:00:00"
    },
    {
      "id": 2,
      "title": "Complete project",
      "description": "Finish the spring boot application",
      "completed": true,
      "userId": 1,
      "createdAt": "2024-12-08T14:30:00",
      "updatedAt": "2024-12-09T09:15:00"
    }
  ],
  "pageNumber": 1,
  "pageSize": 10,
  "totalElements": 2,
  "totalPages": 1
}
```

**Error Response** (401 Unauthorized):

```json
{
  "timestamp": "2024-12-09T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired token",
  "path": "/api/todos/paginatedTodos"
}
```

**cURL Examples**:

Get first page with default settings:

```bash
curl -X GET "http://localhost:8080/api/todos/paginatedTodos" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

Get page 2 with 5 items per page, sorted by title descending:

```bash
curl -X GET "http://localhost:8080/api/todos/paginatedTodos?page=2&size=5&sortBy=title&ascending=false" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

Filter by title (partial match):

```bash
curl -X GET "http://localhost:8080/api/todos/paginatedTodos?title=groceries" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

---

## Error Handling

### Error Response Format

All error responses follow this standard format:

```json
{
  "timestamp": "2024-12-09T10:30:00",
  "status": 400,
  "error": "Error Type",
  "message": "Detailed error message",
  "path": "/api/endpoint"
}
```

### Common HTTP Status Codes

| Status Code | Meaning      | Typical Causes                            |
| ----------- | ------------ | ----------------------------------------- |
| 200         | OK           | Request succeeded                         |
| 201         | Created      | Resource created successfully             |
| 400         | Bad Request  | Invalid input, validation failed          |
| 401         | Unauthorized | Missing or invalid JWT token              |
| 403         | Forbidden    | Access denied                             |
| 404         | Not Found    | Resource not found                        |
| 409         | Conflict     | Resource conflict (e.g., duplicate email) |
| 500         | Server Error | Internal server error                     |

### Common Error Messages

| Message                   | Cause                           | Solution                                  |
| ------------------------- | ------------------------------- | ----------------------------------------- |
| Invalid or expired token  | JWT token is expired or invalid | Login again to get a new token            |
| Email already exists      | Email is already registered     | Use a different email address             |
| Invalid email or password | Credentials are incorrect       | Verify email and password                 |
| Todo not found            | Todo ID doesn't exist           | Verify the todo ID                        |
| Title is required         | Title field is missing          | Provide a title in the request            |
| Unauthorized              | Missing Authorization header    | Include JWT token in Authorization header |

---

## Response Formats

### User Response

```json
{
  "id": 1,
  "email": "user@example.com",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "message": "Operation successful"
}
```

### Todo Response

```json
{
  "id": 1,
  "title": "Task title",
  "description": "Task description",
  "completed": false,
  "userId": 1,
  "createdAt": "2024-12-09T10:00:00",
  "updatedAt": "2024-12-09T10:00:00"
}
```

### Paginated Response

```json
{
  "content": [],
  "pageNumber": 1,
  "pageSize": 10,
  "totalElements": 0,
  "totalPages": 1
}
```

---

## Authentication Flow Diagram

```
1. User Registration
   POST /api/auth/register
   ├─ Validate input
   ├─ Check email uniqueness
   ├─ Hash password with bcrypt
   ├─ Save user to database
   └─ Return JWT token

2. User Login
   POST /api/auth/login
   ├─ Validate credentials
   ├─ Compare password with hash
   ├─ Generate JWT token
   └─ Return token to client

3. Authenticated Request
   GET /api/todos/listTodos
   ├─ Extract token from Authorization header
   ├─ Validate token signature
   ├─ Check token expiration
   ├─ Extract user identity from token
   ├─ Fetch user's todos from database
   └─ Return todos list
```

---

## Rate Limiting

Currently, there is no rate limiting implemented. It's recommended to add rate limiting for production:

```java
// Example: Using Spring Cloud Gateway or similar
@Bean
public RateLimiter rateLimiter() {
    return RateLimiter.create(100.0); // 100 requests per second
}
```

---

## Security Notes

1. **JWT Token Storage**: Store tokens securely on the client (e.g., httpOnly cookies)
2. **HTTPS**: Always use HTTPS in production
3. **Password Requirements**: Enforce strong password policies
4. **Token Expiration**: Tokens expire after 1 hour; refresh tokens should be implemented for production
5. **CORS**: Configure CORS appropriately for your frontend domain
6. **Input Validation**: All inputs are validated server-side

---

## API Versioning

The current API version is **v1** (implicit). Future versions may be introduced with `/api/v2/` prefix.

---

## Support & Troubleshooting

### Testing the API

Use Postman, Insomnia, or similar tools to test the API:

1. Register a user at `/api/auth/register`
2. Login at `/api/auth/login` to get the token
3. Copy the token
4. Set Authorization header: `Bearer <token>`
5. Make requests to todo endpoints

### Common Issues

**Invalid token error even with correct token:**

- Check token hasn't expired
- Verify JWT_SECRET matches between client and server
- Ensure token is in the Authorization header correctly

**CORS errors:**

- Configure CORS in SecurityConfig
- Add appropriate headers to requests

**404 errors:**

- Verify the endpoint URL is correct
- Check HTTP method (GET, POST, PUT, DELETE)
- Ensure the resource exists

---

**Last Updated**: December 9, 2024
**API Version**: 1.0
**Server Version**: Spring Boot 3.4.11
