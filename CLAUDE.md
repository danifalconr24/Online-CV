# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this monorepo.

## Repository Structure

```
online-cv/
├── backend/    # Quarkus 3.5.3 + Java 21 REST API
└── frontend/   # Vue 3 + Quasar 2 SPA
```

---

## Backend (`backend/`)

### Build & Run Commands

```bash
# Dev mode (live reload on port 8080)
./mvnw compile quarkus:dev

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=CurriculumVitaeResourceTest

# Package
./mvnw package

# Run packaged app
java -jar target/quarkus-app/quarkus-run.jar
```

### Stack

Quarkus 3.5.3, Java 21, PostgreSQL, Hibernate ORM Panache, RESTEasy Reactive, SmallRye JWT

### Architecture

Hexagonal (ports & adapters). Package structure (`src/main/java/es/danifalconr/`):

- `domain/model/` — Immutable Java records (WorkExperience, AcademicStudy, GenericInfo, Language, AuthToken)
- `domain/port/out/` — Repository interfaces (out ports) + `TokenService` port
- `domain/exception/` — Domain exceptions (EntityNotFoundException, InvalidCredentialsException, InvalidTokenException)
- `application/` — Use case services (`@ApplicationScoped`, `@Transactional`): WorkExperienceService, AcademicStudyService, GenericInfoService, AuthService
- `infrastructure/rest/` — Thin JAX-RS adapters. No `@Transactional`. Inject application services only.
- `infrastructure/rest/dto/` — Java records for request/response DTOs
- `infrastructure/rest/exception/` — `@Provider` ExceptionMappers mapping domain exceptions to HTTP responses
- `infrastructure/persistence/entity/` — JPA entities extending `PanacheEntity` with `@Table` annotations
- `infrastructure/persistence/mapper/` — Static mapper classes (`toDomain()` / `toEntity()`)
- `infrastructure/persistence/adapter/` — `@ApplicationScoped` Panache repositories implementing domain port interfaces
- `infrastructure/security/` — `SmallRyeJwtTokenService` (implements `TokenService` port)
- `infrastructure/config/` — `SnakeCasePhysicalNamingStrategy` + `Bootstrap` (seeds initial data on dev profile)

**Dependency flow**: REST resources → Application services → Domain ports ← Persistence adapters / Security

### API Endpoints

#### CV API (`/v1/curriculum-vitae/`)

| Method | Path | Auth | Content-Type |
|--------|------|------|--------------|
| GET | `/work-experiences` | `@PermitAll` | JSON |
| POST | `/work-experiences` | `@RolesAllowed("admin")` | JSON |
| PUT | `/work-experiences/{id}` | `@RolesAllowed("admin")` | JSON |
| DELETE | `/work-experiences/{id}` | `@RolesAllowed("admin")` | JSON |
| PUT | `/work-experiences/{id}/logo` | `@RolesAllowed("admin")` | Multipart (`file`) |
| GET | `/academic-studies` | `@PermitAll` | JSON |
| POST | `/academic-studies` | `@RolesAllowed("admin")` | JSON |
| PUT | `/academic-studies/{id}` | `@RolesAllowed("admin")` | JSON |
| DELETE | `/academic-studies/{id}` | `@RolesAllowed("admin")` | JSON |
| GET | `/generic-info` | `@PermitAll` | JSON |
| PUT | `/generic-info/{id}` | `@RolesAllowed("admin")` | JSON |
| PUT | `/generic-info/{id}/image` | `@RolesAllowed("admin")` | Multipart (`file`) |

#### Auth API (`/auth/`)

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/login` | `@PermitAll` | Body: `LoginRequest` → `LoginResponse` |
| POST | `/refresh` | `@PermitAll` | Header: `Authorization: Bearer <token>` → `LoginResponse` |

### Authentication

- JWT via SmallRye JWT (`quarkus-smallrye-jwt` + `quarkus-smallrye-jwt-build`)
- RSA keys in `src/main/resources/privateKey.pem` + `publicKey.pem`
- Admin credentials: `app.admin.username` / `app.admin.password` (env: `ADMIN_USERNAME` / `ADMIN_PASSWORD`, default: `admin` / `admin123`)
- Token duration: 3600s (configurable via `app.jwt.duration`)

### Database

PostgreSQL on `localhost:5432/online-cv-db`. Schema strategy is `drop-and-create` in dev profile. Connection via env vars `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`.

**CORS**: Allowed origin `http://localhost:9000`.

### Testing

JUnit 5 + REST Assured + Mockito. Test HTTP port is randomized (`quarkus.http.test-port=0`). Testcontainers for PostgreSQL in test profile.

- **Unit tests** (Mockito): `WorkExperienceServiceTest`, `AcademicStudyServiceTest`, `GenericInfoServiceTest`
- **Integration tests** (`@QuarkusTest` + REST Assured): `WorkExperienceResourceTest`, `AcademicStudyResourceTest`, `GenericInfoResourceTest`, `AuthResourceTest`, `CurriculumVitaeResourceTest`
- **Auth tests**: `AuthServiceTest` (`@QuarkusTest` + `@InjectMock`)

### Backend Coding Standards

- **Java Streams API**: Prefer functional programming with streams for collections processing
- **Records over Lombok**: Prioritize Java records; use Lombok (`@Data`, `@Builder`, etc.) only when records are insufficient
- **Virtual threads**: Use Java 21 virtual threads on every new API request
- **Naming**: Descriptive names in English for all variables and classes
- **Principles**: SOLID + Hexagonal Architecture best practices, KISS

---

## Frontend (`frontend/`)

### Build & Development Commands

```bash
npm run dev      # Start Quasar dev server with hot-reload (port 9000)
npm run build    # Production build to dist/spa
npm run lint     # ESLint on .js, .ts, .vue files
npm run format   # Prettier formatting
```

No test suite is configured.

### Stack

Vue 3 + Quasar 2 SPA, built with Vite and TypeScript.

### Architecture & Key Patterns

- **Composition API** with `<script setup>` throughout
- **No state management library** — components use local `ref()` state
- **Hash-based routing** (configured in `quasar.config.js`)
- **Direct fetch calls** to `http://localhost:8080/v1/curriculum-vitae/` — no API abstraction layer
- **Quasar utility classes** for layout/styling; SCSS for custom styles

### Data Flow

`MainPage.vue` fetches all data in `onBeforeMount` from three endpoints:
- `/academic-studies` → `AcademicStudy[]`
- `/work-experiences` → `WorkExperience[]`
- `/generic-info` → `GenericInfo`

Data is passed as props to section components (`AcademicStudiesSection`, `WorkExperiencesSection`).

### Key Files

- `src/composables/useAuth.ts` — auth state (token in localStorage), login/logout/authHeaders
- `src/pages/LoginPage.vue` — login form at `/login`
- `src/pages/MainPage.vue` — main CV view, login/logout button, editable About Me
- `src/components/WorkExperiencesSection.vue` — CRUD UI with inline edit + add form
- `src/components/AcademicStudiesSection.vue` — CRUD UI with inline edit + add form
- `src/components/types/models.ts` — TypeScript interfaces for all data models
- `src/css/app.scss` — global SCSS styles
- `src/css/quasar.variables.scss` — Quasar theme variables

### Frontend Coding Standards

- Single quotes, semicolons (`.prettierrc`)
- 2-space indentation, LF line endings
- PascalCase for component filenames and component names
- ESLint extends `@typescript-eslint/recommended`, `plugin:vue/vue3-essential`, `prettier`
