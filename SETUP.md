# Placement Portal - Backend Setup Guide

## Prerequisites

- Java 21 or higher
- MySQL Server
- Maven (included as mvnw.cmd)

## Setup Instructions

### 1. Database Setup

Create MySQL database:
```sql
CREATE DATABASE placement_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Backend Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/placement_db
spring.datasource.username=root
spring.datasource.password=your_password

# Mailgun Configuration (Optional)
mailgun.domain=your-domain.mailgun.org
mailgun.api-key=your_api_key
mailgun.from=noreply@nexstep.com

# JWT Secret (Keep it long and secure)
app.jwt.secret=your-super-secret-key-that-is-at-least-32-characters-long

# File Upload Path
file.upload.path=./uploads
file.upload.url=http://localhost:8080
```

### 3. Building the Backend

```bash
cd backend
.\mvnw.cmd clean package
```

### 4. Running the Backend

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

The backend will start on `http://localhost:8080`

## API Endpoints Overview

### Authentication `/api/auth`
- `POST /login` - User login
- `POST /signup` - User registration
- `POST /refresh-token` - Refresh JWT token
- `GET /validate` - Validate JWT token

### User Management `/api/users`
- `GET /` - Get all users
- `GET /{id}` - Get user by ID
- `GET /email/{email}` - Get user by email
- `PUT /{id}` - Update user

### Jobs `/api/jobs`
- `GET /` - Get all active jobs
- `GET /{id}` - Get job by ID
- `GET /company/{company}` - Get jobs by company
- `POST /` - Create job
- `PUT /{id}` - Update job
- `DELETE /{id}` - Delete job

### Applications `/api/applications`
- `POST /` - Create application
- `GET /user/{userId}` - Get user's applications
- `GET /job/{jobId}` - Get job's applications
- `GET /{applicationId}` - Get application details
- `PUT /{applicationId}/status` - Update application status
- `DELETE /{applicationId}` - Withdraw application

### Student Profile `/api/student-profile`
- `GET /{userId}` - Get student profile
- `POST /{userId}` - Create profile
- `PUT /{userId}` - Update profile

### Notifications `/api/notifications`
- `POST /email` - Send email notification
- `GET /history` - Get notification history
- `GET /pending` - Get pending notifications
- `GET /{id}` - Get notification details

### File Upload `/api/files`
- `POST /upload` - Upload file
- `GET /user/{userId}` - Get user's files
- `GET /user/{userId}/type/{fileType}` - Get latest file by type
- `DELETE /{fileId}` - Delete file

## Database Schema

### Users Table
- id: Long (Primary Key)
- email: String (Unique)
- password: String (Hashed)
- fullName: String
- role: Enum (STUDENT, EMPLOYER, PLACEMENT_OFFICER, ADMIN)
- phone: String
- companyName: String (for employers)
- active: Boolean
- emailVerified: Boolean
- createdAt: Timestamp
- updatedAt: Timestamp

### Student Profiles Table
- id: Long (Primary Key)
- userId: Long (Foreign Key)
- phone: String
- university: String
- department: String
- passoutYear: String
- cgpa: String
- skills: Text
- bio: Text
- profilePictureUrl: String
- resumeUrl: String
- createdAt: Timestamp
- updatedAt: Timestamp

### Jobs Table
- id: Long (Primary Key)
- title: String
- description: Text
- company: String
- location: String
- salary: Double
- postedBy: Long (Foreign Key - User ID)
- postedDate: Date
- active: Boolean

### Applications Table
- id: Long (Primary Key)
- jobId: Long (Foreign Key)
- userId: Long (Foreign Key)
- status: Enum (PENDING, ACCEPTED, REJECTED, SHORTLISTED)
- appliedDate: Date
- resume: String (URL)

### Notifications Table
- id: Long (Primary Key)
- recipientEmail: String
- subject: String
- message: Text
- type: String
- mailgunId: String
- status: Enum (PENDING, SENT, FAILED)
- metadata: Text (JSON)
- sentAt: Timestamp
- createdAt: Timestamp
- updatedAt: Timestamp

### File Uploads Table
- id: Long (Primary Key)
- userId: Long (Foreign Key)
- originalFileName: String
- fileName: String
- fileUrl: String
- fileType: String
- fileSize: Long
- uploadedAt: Timestamp
- updatedAt: Timestamp

## Security Features

- JWT (JSON Web Token) authentication
- BCrypt password encryption
- Role-based access control
- CORS configuration
- Input validation
- Exception handling

## Mailgun Integration

To enable email notifications:

1. Sign up at [Mailgun](https://mailgun.com)
2. Get your API key and domain
3. Update `application.properties` with your credentials
4. The system will send emails automatically

If Mailgun is not configured, emails will be logged (no-op mode).

## Testing

### Login Example
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "password": "password123",
    "role": "STUDENT"
  }'
```

### Create Job Example
```bash
curl -X POST http://localhost:8080/api/jobs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Software Engineer",
    "description": "Looking for experienced engineers",
    "company": "Tech Corp",
    "location": "New York",
    "salary": 100000
  }'
```

## Troubleshooting

1. **Port already in use**: Change port in application.properties
2. **Database connection failed**: Check MySQL service and credentials
3. **JWT errors**: Ensure JWT secret is set correctly
4. **CORS errors**: Check allowed origins in SecurityConfig

## Support

For issues or questions, contact the development team.
