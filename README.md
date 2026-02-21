# Online CV

A personal online curriculum vitae application with a Quarkus backend API and a Vue.js/Quasar frontend.

## Tech Stack

| Layer    | Technology                                              |
|----------|---------------------------------------------------------|
| Backend  | Java 21, Quarkus 3.5, Hibernate ORM Panache, PostgreSQL |
| Frontend | Vue.js 3, Quasar Framework                              |
| Database | PostgreSQL                                              |

## Project Structure

```
online-cv/
├── backend/    # Quarkus REST API (hexagonal architecture)
└── frontend/   # Quasar/Vue.js SPA
```

The backend follows **hexagonal architecture** (ports & adapters):

- `domain/` — Pure POJOs and repository port interfaces
- `application/` — Use case services
- `infrastructure/` — REST adapters, DTOs, JPA persistence, configuration

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

The backend seeds initial data (about me, work experiences, academic studies) on startup.

### Frontend

```bash
cd frontend

# Install dependencies
npm install

# Start dev server (port 9000)
npx quasar dev
```

## API Endpoints

Base path: `/v1/curriculum-vitae/`

| Method | Path                 | Description              |
|--------|----------------------|--------------------------|
| GET    | `/generic-info`      | Get about me information |
| GET    | `/work-experiences`  | List work experiences    |
| POST   | `/work-experiences`  | Create a work experience |
| GET    | `/academic-studies`  | List academic studies    |
| POST   | `/academic-studies`  | Create an academic study |

## Configuration

The backend uses environment variables with sensible defaults for local development:

| Variable            | Default                                       |
|---------------------|-----------------------------------------------|
| `DATABASE_URL`      | `jdbc:postgresql://localhost:5432/online-cv-db` |
| `DATABASE_USERNAME` | `danifalconr`                                 |
| `DATABASE_PASSWORD` | `dev-online-cv-passwd`                        |

CORS is configured to allow requests from `http://localhost:9000`.
