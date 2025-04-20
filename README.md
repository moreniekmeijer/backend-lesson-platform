# Lesson Platform - Backend API (Spring Boot)

## Table of Contents
1. [Introduction](#1-introduction)
2. [Tech Requirements](#2-tech-requirements)
3. [Setup Steps](#3-setup-steps)
4. [Folder Structure](#4-folder-structure)
5. [Database Configuration](#5-database-configuration)
6. [Application Properties](#6-application-properties)
7. [Test Roles](#7-test-roles)
8. [Authentication](#8-authentication)
9. [API Documentation](#9-api-documentation)
10. [What Next?](#10-what-next)

---

## 1. Introduction

This Spring Boot API provides a structured way for teachers to upload and manage lesson materials, and for students to easily search and access them. It improves on existing file-sharing systems by offering category-based filtering, user management, and a better UX for educational material.

It is designed to be used with the Lesson Platform frontend, which can be found here:
**[frontend-lesson-platform 🔗](https://github.com/moreniekmeijer/frontend-lesson-platform)**

---

## 2. Tech Requirements
Make sure the following tools are installed:

| Tool          | Link                                                       |
|---------------|------------------------------------------------------------|
| IntelliJ IDEA | [Download](https://www.jetbrains.com/idea/download)        |
| JDK 21        | [Download](https://www.oracle.com/java/technologies/downloads/#java21) |
| PostgreSQL    | [Download](https://www.postgresql.org/download/)           |
| PgAdmin 4     | [Download](https://www.pgadmin.org/download/)              |
| Postman       | [Download](https://www.postman.com/downloads/)             |

---

## 3. Setup Steps

### Option A: Download ZIP
- Download the ZIP file provided with the source code
- Extract it to your preferred directory
- Open it in IntelliJ IDEA as a Maven project

### Option B: Clone Repository
```bash
git clone https://github.com/moreniekmeijer/lesson-platform.git
cd lesson-platform
```

### General Setup
1. Open project in IntelliJ IDEA
2. Ensure you have JDK 21 set as the SDK
3. Start PostgreSQL (via pgAdmin or CLI)
4. Create a new database called `lesson-platform`
5. Run the application
6. Check if the application created a folder 'uploads'
7. If not; you can add this folder manually from the attached ZIP file
8. If so; This folder can be pre-filled with the items in the 'uploads' folder in the attached ZIP file

---

## 4. Project Structure

For a complete view of the project structure, visit this link: `https://githubtree.mgks.dev/repo/moreniekmeijer/backend-lesson-platform/main/`

---

## 5. Database Configuration

1. Open **pgAdmin 4**
2. Connect to your server
3. Right-click on Databases → Create → Database
4. Name it `lesson-platform`

---

## 6. Application Properties

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=lesson-platform

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/lesson-platform
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.database=postgresql
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true

# JWT
jwt.secret=YOUR_SECRET
jwt.expiration=86400000

# File upload
file.upload-location=uploads/
spring.servlet.multipart.max-file-size=200MB
spring.servlet.multipart.max-request-size=200MB

# Invite Code
invite.code=lekkerwaterdichtregistreren
```

> Replace `YOUR_DB_USERNAME`, `YOUR_DB_PASSWORD`, and `YOUR_SECRET` with your own values.

To generate a strong JWT secret (to be placed in `YOUR_SECRET`), you can use RandomKeygen or a similar online tool.

---

## 7. Test Roles

| Role  | Username | Email         | Password |
|-------|----------|---------------|----------|
| USER  | user     | user@test.nl  | password |
| ADMIN | admin    | admin@test.nl | password |

If you want to register a new user, you should use the invite.code: 'lekkerwaterdichtregistreren'.

---

## 8. Authentication

This API uses JWT (JSON Web Token) for stateless authentication.

### Authenticate in Postman
1. Send POST request to `/authenticate`:

```json
{
  "username": "admin",
  "password": "password"
}
```

2. You'll receive a response:
```json
{
  "token": "your.jwt.token"
}
```

3. Use token in the header of future requests:
```
Authorization: Bearer your.jwt.token
```
If you downloaded the **[Postman Docs 🔗](https://documenter.getpostman.com/view/41365945/2sB2cd4yGR)**, you can also put this token in the {{admin_role}} variable in the 'Lesson Platform' environment.

---

## 9. API Documentation

The full API documentation is available in Postman:  
**[Postman Docs 🔗](https://documenter.getpostman.com/view/41365945/2sB2cd4yGR)**

### Core Endpoints (Summary)

#### Authentication
- `POST /authenticate` ➔ login and returns JWT token

#### Users
- `GET /users` ➔ List all users (admin only)
- `GET /users/{id}` ➔ Get user by ID
- `POST /users/register` ➔ Register a new user (with invite code, see: [Application Properties](#5-application-properties))

#### Materials
- `GET /materials` ➔ List all materials
- `GET /materials/{id}` ➔ Get one material
- `POST /materials` ➔ Upload new material-metadata (use in combination with the 'Assign file or link')
- `DELETE /materials/{id}` ➔ Delete material

#### Assign file or link
- `POST /materials/{id}/file` ➔ Add file (PDF, audio, etc.)
- `POST /materials/{id}/link` ➔ Add link

#### Style Management
- `GET /styles` ➔ Get all styles
- `POST /styles` ➔ Add a new style
- `DELETE /styles/{id}` ➔ Delete style

#### Lesson Management
- `GET /lessons/next` ➔ Get upcoming lesson
- `POST /lessons` ➔ Add a new lesson
- `DELETE /lessons/{id}` ➔ Delete lesson

> For full request/response bodies and headers, refer to [Postman Docs](https://documenter.getpostman.com/view/41365945/2sB2cd4yGR)

---

## 10. What Next?

After you successfully run the backend for the Lesson Platform, head over to the [Frontend](https://github.com/moreniekmeijer/frontend-lesson-platform) to start testing the backend in combination with the frontend.

---

Feel free to open issues or suggestions for improvements!
