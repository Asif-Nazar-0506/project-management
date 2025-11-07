# Project Management REST API

A comprehensive Mini Project Management REST API built with Spring Boot 3.x, featuring user authentication, project management, and task tracking with advanced filtering and search capabilities.

## 🚀 Features

- **User Management**
  - User registration and login
  - JWT-based authentication
  - Secure password encryption with BCrypt

- **Project Management**
  - Create, read, update, and delete projects
  - User-specific project isolation
  - Automatic timestamp tracking

- **Task Management**
  - Full CRUD operations for tasks
  - Task status: PENDING, IN_PROGRESS, COMPLETED
  - Task priority: LOW, MEDIUM, HIGH
  - Due date tracking
  - Advanced filtering by status and priority
  - Search across all user tasks
  - Sorting by dueDate or priority

- **Security**
  - JWT token-based authentication
  - Spring Security integration
  - Role-based access control
  - Protected endpoints

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **H2 In-Memory Database**
- **Maven**
- **Lombok**
- **Bean Validation**

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+

## 🏃‍♂️ Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd project-management-api
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access H2 Console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:projectdb`
- Username: `sa`
- Password: *(leave empty)*

## 📡 API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register a new user | No |
| POST | `/api/auth/login` | Login user | No |

### Projects

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/projects` | Create a new project | Yes |
| GET | `/api/projects` | Get all user projects | Yes |
| GET | `/api/projects/{id}` | Get project by ID | Yes |
| PUT | `/api/projects/{id}` | Update project | Yes |
| DELETE | `/api/projects/{id}` | Delete project | Yes |

### Tasks

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/projects/{projectId}/tasks` | Create a new task | Yes |
| GET | `/api/projects/{projectId}/tasks` | Get all tasks (with filters) | Yes |
| GET | `/api/projects/{projectId}/tasks/{taskId}` | Get task by ID | Yes |
| PUT | `/api/projects/{projectId}/tasks/{taskId}` | Update task | Yes |
| DELETE | `/api/projects/{projectId}/tasks/{taskId}` | Delete task | Yes |
| GET | `/api/tasks/search?query={query}&sortBy={field}` | Search tasks across all projects | Yes |

### Query Parameters for Task Filtering

- `status`: Filter by task status (PENDING, IN_PROGRESS, COMPLETED)
- `priority`: Filter by priority (LOW, MEDIUM, HIGH)
- `sortBy`: Sort by field (dueDate, priority, createdAt)
- `query`: Search term for title/description

## 📝 Request/Response Examples

### Register User

**Request:**
```json
POST /api/auth/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "email": "john@example.com"
}
```

### Create Project

**Request:**
```json
POST /api/projects
Authorization: Bearer {token}
{
  "name": "E-commerce Website",
  "description": "Build a modern e-commerce platform"
}
```

**Response:**
```json
{
  "id": 1,
  "name": "E-commerce Website",
  "description": "Build a modern e-commerce platform",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Create Task

**Request:**
```json
POST /api/projects/1/tasks
Authorization: Bearer {token}
{
  "title": "Design database schema",
  "description": "Create ER diagram and define tables",
  "status": "PENDING",
  "priority": "HIGH",
  "dueDate": "2024-01-20"
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Design database schema",
  "description": "Create ER diagram and define tables",
  "status": "PENDING",
  "priority": "HIGH",
  "dueDate": "2024-01-20",
  "projectId": 1,
  "createdAt": "2024-01-15T10:35:00",
  "updatedAt": "2024-01-15T10:35:00"
}
```

### Filter Tasks

```
GET /api/projects/1/tasks?status=IN_PROGRESS&priority=HIGH&sortBy=dueDate
Authorization: Bearer {token}
```

### Search Tasks

```
GET /api/tasks/search?query=database&sortBy=priority
Authorization: Bearer {token}
```

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### Projects Table
```sql
CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Tasks Table
```sql
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    due_date DATE,
    project_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id)
);
```

## 🔒 Security

- All endpoints except `/api/auth/**` require JWT authentication
- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours (configurable)
- Users can only access their own projects and tasks

## ✅ Validation

- Username: 3-50 characters, required
- Email: Valid email format, required
- Password: Minimum 6 characters, required
- Project name: Required, max 100 characters
- Task title: Required, max 200 characters
- Status and Priority: Must be valid enum values

## 🧪 Testing with Postman

1. Import the provided Postman collection
2. Register a new user
3. Copy the JWT token from the response
4. Set the token in Authorization header for subsequent requests
5. Create projects and tasks

## 📦 Project Structure

```
src/main/java/com/projectmgmt/
├── ProjectManagementApplication.java
├── controller/
│   ├── AuthController.java
│   ├── ProjectController.java
│   ├── TaskController.java
│   └── UserTaskController.java
├── dto/
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── AuthResponse.java
│   ├── ProjectRequest.java
│   ├── ProjectResponse.java
│   ├── TaskRequest.java
│   ├── TaskResponse.java
│   └── ErrorResponse.java
├── entity/
│   ├── User.java
│   ├── Project.java
│   └── Task.java
├── enums/
│   ├── Status.java
│   └── Priority.java
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ResourceAlreadyExistsException.java
│   ├── UnauthorizedException.java
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── UserRepository.java
│   ├── ProjectRepository.java
│   └── TaskRepository.java
├── security/
│   ├── JwtUtil.java
│   ├── JwtAuthFilter.java
│   └── SecurityConfig.java
└── service/
    ├── AuthService.java
    ├── ProjectService.java
    └── TaskService.java
```

## 🚨 Error Handling

The API returns appropriate HTTP status codes and error messages:

- **400 Bad Request**: Validation errors
- **401 Unauthorized**: Invalid credentials
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **409 Conflict**: Resource already exists
- **500 Internal Server Error**: Unexpected errors

## 🔧 Configuration

Key configuration properties in `application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:h2:mem:projectdb

# JWT
jwt.secret=your-secret-key
jwt.expiration=86400000  # 24 hours
```

## 👥 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 👤 Author
Asif Nazar
