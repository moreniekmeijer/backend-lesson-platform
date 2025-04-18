# Lesson Platform - Backend API (Spring Boot)

## Table of Contents
1. [Introduction](#1-introduction)
2. [Tech Requirements](#2-tech-requirements)
3. [Setup Steps](#3-setup-steps)
4. [Database Configuration](#4-database-configuration)
5. [Application Properties](#5-application-properties)
6. [Test Roles](#6-test-roles)
7. [Authentication](#7-authentication)
8. [API Documentation](#8-api-documentation)
9. [More](#9-more)

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
- Open it in IntelliJ IDEA as a Maven/Gradle project

### Option B: Clone Repository (if hosted)
```bash
git clone https://github.com/yourusername/lesson-platform.git
cd lesson-platform
```

### General Setup
1. Open project in IntelliJ IDEA
2. Ensure you have JDK 21 set as the SDK
3. Start PostgreSQL (via pgAdmin or CLI)
4. Create a new database called `lesson-platform`
5. Run the application
6. Check if the application created a folder 'uploads' 
7. This folder can be pre-filled with the items in the 'uploads' map in the attached ZIP file

---

## 4. Database Configuration

1. Open **pgAdmin 4**
2. Connect to your server
3. Right-click on Databases → Create → Database
4. Name it `lesson-platform`

---

## 5. Application Properties

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

## 6. Test Roles

| Role  | Username | Email         | Password |
|-------|----------|---------------|----------|
| USER  | user     | user@test.nl  | password |
| ADMIN | admin    | admin@test.nl | password |

---

## 7. Authentication

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

## 8. API Documentation

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

## 9. More

For the **Dutch version** of this documentation in PDF format, see [Installatiehandleiding (NL)](link-to-pdf).

---

Feel free to open issues or suggestions for improvements!

[//]: # (# 📘 API Documentatie – Lesson Platform)

[//]: # ()
[//]: # (Deze documentatie beschrijft de beschikbare endpoints voor de backend van het lesplatform.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## /users)

[//]: # ()
[//]: # (**Beschrijving:** Endpoints voor gebruikersbeheer: registratie, ophalen, bijwerken en verwijderen van gebruikers.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### POST /users)

[//]: # (Registreer een nieuwe gebruiker. Iedereen mag deze endpoint aanroepen zonder authenticatie. Een JWT wordt direct teruggegeven na succesvolle registratie.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /users)

[//]: # (Ophalen van een lijst met alle geregistreerde gebruikers. Alleen beschikbaar voor beheerders &#40;ROLE_ADMIN&#41;.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /users/{username})

[//]: # (Bekijk gegevens van een specifieke gebruiker. Alleen toegankelijk voor de gebruiker zelf of een beheerder.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### PUT /users/{username})

[//]: # (Werk gebruikersgegevens bij. Alleen de gebruiker zelf mag dit uitvoeren.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### DELETE /users/{username})

[//]: # (Verwijder een gebruiker. Alleen de gebruiker zelf of een beheerder mag dit uitvoeren.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /users/{username}/authorities)

[//]: # (Haal alle rollen &#40;authorities&#41; van een gebruiker op. Alleen beschikbaar voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### POST /users/{username}/authorities)

[//]: # (Voeg een rol toe aan een gebruiker. Alleen beschikbaar voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### DELETE /users/{username}/authorities/{authority})

[//]: # (Verwijder een specifieke rol van een gebruiker. Alleen beschikbaar voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### PUT /users/{username}/materials/{materialId})

[//]: # (Koppel een specifiek lesmateriaal aan een gebruiker. Alleen de gebruiker zelf mag dit uitvoeren.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /users/{username}/materials)

[//]: # (Haal alle opgeslagen materialen op voor de ingelogde gebruiker. Alleen de gebruiker zelf mag dit uitvoeren.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### DELETE /users/{username}/materials/{materialId})

[//]: # (Verwijder een gekoppeld lesmateriaal uit het overzicht van de gebruiker. Alleen de gebruiker zelf mag dit uitvoeren.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## /materials)

[//]: # ()
[//]: # (**Beschrijving:** Endpoints voor het beheren van lesmateriaal zoals video's, PDF's of audiofragmenten.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### POST /materials)

[//]: # (Voeg een nieuw lesmateriaal toe. Alleen toegankelijk voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /materials)

[//]: # (Haal een overzicht op van alle lesmaterialen. Alleen beschikbaar voor geauthenticeerde gebruikers.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /materials/{id})

[//]: # (Haal de details op van een specifiek lesmateriaal.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### POST /materials/{id}/file)

[//]: # (Upload een bestand &#40;zoals video of PDF&#41; bij een bestaand lesmateriaal. Alleen beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /materials/{id}/file)

[//]: # (Download het bestand dat is gekoppeld aan het materiaal.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /materials/{id}/file?action=download)

[//]: # (Download het bestand forcerend als download &#40;met content-disposition&#41;.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### POST /materials/{id}/link)

[//]: # (Koppel een externe link aan het lesmateriaal.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /materials?origin={country})

[//]: # (Haal alle materialen op, gefilterd op het land van herkomst.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### DELETE /materials/{id})

[//]: # (Verwijder een specifiek lesmateriaal. Alleen voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## /styles)

[//]: # ()
[//]: # (**Beschrijving:** Endpoints voor het beheren van muziekstijlen. Denk aan genres zoals samba, makru of maracatu.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### POST /styles)

[//]: # (Voeg een nieuwe stijl toe. Alleen toegankelijk voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /styles)

[//]: # (Haal een lijst op van alle beschikbare stijlen.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /styles/{id})

[//]: # (Haal de details op van één stijl via ID.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### DELETE /styles/{id})

[//]: # (Verwijder een stijl. Alleen voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### PUT /styles/{styleId}/materials/{materialId})

[//]: # (Koppel bestaand lesmateriaal aan een stijl. Alleen toegankelijk voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## /lessons)

[//]: # ()
[//]: # (**Beschrijving:** Endpoints voor het beheren van lessen, hun tijdstip, notities en bijbehorende stijlen.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### POST /lessons)

[//]: # (Plan een nieuwe les in. Alleen voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /lessons)

[//]: # (Haal alle geplande lessen op. Alleen toegankelijk voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /lessons/next)

[//]: # (Haal de eerstvolgende les op &#40;volgende op datum/tijd&#41;.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /lessons/{id})

[//]: # (Verwijder een geplande les op basis van ID. Alleen voor beheerders.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## /authenticate)

[//]: # ()
[//]: # (**Beschrijving:** Endpoints voor authenticatie en tokenvalidatie.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### POST /authenticate &#40;admin&#41;)

[//]: # (Login als admin-gebruiker. JWT-token wordt teruggegeven.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### POST /authenticate &#40;user&#41;)

[//]: # (Login als normale gebruiker. JWT-token wordt teruggegeven.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### GET /authenticated)

[//]: # (Controleer of de huidige JWT geldig is.)
