# SkillBridge Backend

A Spring Boot application for the SkillBridge platform that connects mentors and mentees.

## Technology Stack

- Java 21
- Spring Boot 3.5.0
- Spring Security with JWT
- PostgreSQL
- Maven
- Docker

## Environment Variables

Create a `.env` file in the root directory with the following variables:

```bash
# Database Configuration
DATABASE_URL=jdbc:postgresql://your-database-host:5432/your-database-name?sslmode=require
DATABASE_USERNAME=your-database-username
DATABASE_PASSWORD=your-database-password

# JWT Configuration
JWT_SECRET=your-super-secure-jwt-secret-key-here-make-it-long-and-random

# Server Configuration (optional)
PORT=8080

# Application Profile
SPRING_PROFILES_ACTIVE=local
```

## Running the Application

### Local Development
```bash
mvn spring-boot:run
```

### Using Docker Compose (Recommended for Development)
```bash
# Start the application with PostgreSQL
docker-compose up --build

# Stop the application
docker-compose down
```

### Using Docker
```bash
docker build -t skillbridge-backend .
docker run -p 8080:8080 skillbridge-backend
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user

### Projects
- `POST /project` - Create a new project
- `GET /project` - Get user's projects
- `GET /project/feed` - Get all projects for feed
- `GET /project/{id}` - Get project by ID
- `POST /project/edit` - Edit project

## Security

- JWT-based authentication
- Password encryption using BCrypt
- CORS configured for frontend integration
- Input validation on all endpoints
- Environment variable configuration for sensitive data

## Validation Rules

### User Registration
- Name: 2-50 characters
- Email: Valid email format
- Password: Minimum 8 characters with uppercase, lowercase, number, and special character
- Role: Must be MENTOR or MENTEE
- Bio: Maximum 500 characters
- Skills: Maximum 200 characters
- GitHub/LinkedIn: Valid URL format

### Project Creation/Editing
- Title: 3-100 characters
- Description: 10-2000 characters
- Tags: Maximum 200 characters
- Repository URL: Valid GitHub repository URL format

## Testing

Run the tests with:
```bash
mvn test
```

## Recent Improvements

- ✅ Moved sensitive data to environment variables
- ✅ Added comprehensive input validation
- ✅ Enhanced error handling with standardized responses
- ✅ Improved security configuration
- ✅ Added constructor injection for better practices
- ✅ Created unit tests
- ✅ Added Docker Compose for easy development
- ✅ Enhanced documentation

## Error Handling

The application now provides standardized error responses with the following format:

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:30:00",
  "details": {
    "fieldName": "Error message"
  }
}
``` 