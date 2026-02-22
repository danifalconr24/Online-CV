# Online CV

A personal online curriculum vitae application with a Quarkus backend API and a Vue.js/Quasar frontend. Features JWT-protected admin CRUD operations, image/logo uploads, and a responsive SPA.

## Tech Stack

| Layer    | Technology                                                        |
|----------|-------------------------------------------------------------------|
| Backend  | Java 21, Quarkus 3.5, Hibernate ORM Panache, SmallRye JWT        |
| Frontend | Vue.js 3, Quasar 2, Vite, TypeScript                             |
| Database | PostgreSQL                                                        |

## Project Structure

```
online-cv/
├── backend/    # Quarkus REST API (hexagonal architecture)
└── frontend/   # Quasar/Vue.js SPA
```

The backend follows **hexagonal architecture** (ports & adapters):

- `domain/` — Immutable Java records, repository port interfaces, domain exceptions
- `application/` — Use case services (WorkExperience, AcademicStudy, GenericInfo, Auth)
- `infrastructure/` — REST adapters, DTOs, exception mappers, JPA persistence, JWT security, configuration

## Prerequisites

- Java 21
- Node.js (for frontend)
- PostgreSQL running on `localhost:5432` with a database named `online-cv-db`

## Getting Started

### Backend

```bash
cd backend

# Start in dev mode (live reload on port 8080)
./mvnw compile quarkus:dev

# Run tests
./mvnw test

# Package
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

The backend seeds initial data (about me, work experiences, academic studies) on startup via the `Bootstrap` class.

### Frontend

```bash
cd frontend

# Install dependencies
npm install

# Start dev server (port 9000)
npm run dev

# Lint
npm run lint

# Production build
npm run build
```

## API Endpoints

### CV API — Base path: `/v1/curriculum-vitae/`

| Method | Path                          | Auth    | Description                          |
|--------|-------------------------------|---------|--------------------------------------|
| GET    | `/work-experiences`           | Public  | List all work experiences            |
| POST   | `/work-experiences`           | Admin   | Create a work experience             |
| PUT    | `/work-experiences/{id}`      | Admin   | Update a work experience             |
| DELETE | `/work-experiences/{id}`      | Admin   | Delete a work experience             |
| PUT    | `/work-experiences/{id}/logo` | Admin   | Upload company logo (multipart)      |
| GET    | `/academic-studies`           | Public  | List all academic studies            |
| POST   | `/academic-studies`           | Admin   | Create an academic study             |
| PUT    | `/academic-studies/{id}`      | Admin   | Update an academic study             |
| DELETE | `/academic-studies/{id}`      | Admin   | Delete an academic study             |
| GET    | `/generic-info`               | Public  | Get about me information             |
| PUT    | `/generic-info/{id}`          | Admin   | Update about me information          |
| PUT    | `/generic-info/{id}/image`    | Admin   | Upload profile image (multipart)     |

### Auth API — Base path: `/auth/`

| Method | Path       | Description                                  |
|--------|------------|----------------------------------------------|
| POST   | `/login`   | Authenticate with username/password           |
| POST   | `/refresh` | Refresh token via `Authorization` header      |

## Authentication

Admin operations (POST, PUT, DELETE) are protected with JWT. Public endpoints (GET) require no authentication.

1. Login via `POST /auth/login` with `{ "username": "...", "password": "..." }`
2. Receive `{ "accessToken": "...", "tokenType": "Bearer", "expiresIn": 3600 }`
3. Include `Authorization: Bearer <token>` header on protected requests

## Configuration

The backend uses environment variables with sensible defaults for local development:

| Variable            | Default                                         | Description        |
|---------------------|------------------------------------------------|--------------------|
| `DATABASE_URL`      | `jdbc:postgresql://localhost:5432/online-cv-db` | JDBC connection URL |
| `DATABASE_USERNAME` | `danifalconr`                                   | DB username        |
| `DATABASE_PASSWORD` | `dev-online-cv-passwd`                          | DB password        |
| `ADMIN_USERNAME`    | `admin`                                         | Admin login user   |
| `ADMIN_PASSWORD`    | `admin123`                                      | Admin login pass   |

CORS is configured to allow requests from `http://localhost:9000`.
