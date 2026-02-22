# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

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

## Architecture

**Stack**: Quarkus 3.5.3, Java 21, PostgreSQL, Hibernate ORM Panache, RESTEasy Reactive, SmallRye JWT

**Architecture**: Hexagonal (ports & adapters) with three layers:

**Package structure** (`src/main/java/es/danifalconr/`):
- `domain/model/` — Immutable Java records (WorkExperience, AcademicStudy, GenericInfo, Language, AuthToken)
- `domain/port/out/` — Repository interfaces (out ports) defining persistence contracts + `TokenService` port
- `domain/exception/` — Domain exceptions (EntityNotFoundException, InvalidCredentialsException, InvalidTokenException)
- `application/` — Use case services (`@ApplicationScoped`, `@Transactional`): WorkExperienceService, AcademicStudyService, GenericInfoService, AuthService
- `infrastructure/rest/` — Thin REST adapters (JAX-RS endpoints). No `@Transactional`. Inject application services.
- `infrastructure/rest/dto/` — Java records for request/response DTOs
- `infrastructure/rest/exception/` — `@Provider` ExceptionMappers mapping domain exceptions to HTTP responses
- `infrastructure/persistence/entity/` — JPA entities extending `PanacheEntity` with `@Table` annotations
- `infrastructure/persistence/mapper/` — Static mapper classes (`toDomain()`/`toEntity()`)
- `infrastructure/persistence/adapter/` — `@ApplicationScoped` Panache repository classes implementing domain port interfaces + `PanacheRepository<Entity>`
- `infrastructure/security/` — `SmallRyeJwtTokenService` (implements `TokenService` port)
- `infrastructure/config/` — `SnakeCasePhysicalNamingStrategy` + `Bootstrap` (seeds initial data on startup)

**Dependency flow**: REST resources → Application services → Domain ports ← Persistence adapters / Security implementations

## API Endpoints

### CV API (`/v1/curriculum-vitae/`)

| Method | Path | Auth | Content-Type |
|--------|------|------|--------------|
| GET | `/work-experiences` | `@PermitAll` | JSON |
| POST | `/work-experiences` | `@RolesAllowed("admin")` | JSON |
| PUT | `/work-experiences/{id}` | `@RolesAllowed("admin")` | JSON |
| DELETE | `/work-experiences/{id}` | `@RolesAllowed("admin")` | JSON |
| PUT | `/work-experiences/{id}/logo` | `@RolesAllowed("admin")` | Multipart (field: `file`) |
| GET | `/academic-studies` | `@PermitAll` | JSON |
| POST | `/academic-studies` | `@RolesAllowed("admin")` | JSON |
| PUT | `/academic-studies/{id}` | `@RolesAllowed("admin")` | JSON |
| DELETE | `/academic-studies/{id}` | `@RolesAllowed("admin")` | JSON |
| GET | `/generic-info` | `@PermitAll` | JSON |
| PUT | `/generic-info/{id}` | `@RolesAllowed("admin")` | JSON |
| PUT | `/generic-info/{id}/image` | `@RolesAllowed("admin")` | Multipart (field: `file`) |

### Auth API (`/auth/`)

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/login` | `@PermitAll` | Body: `LoginRequest` → `LoginResponse` |
| POST | `/refresh` | `@PermitAll` | Header: `Authorization: Bearer <token>` → `LoginResponse` |

## Authentication

- **JWT** via SmallRye JWT (`quarkus-smallrye-jwt` + `quarkus-smallrye-jwt-build`)
- RSA keys in `src/main/resources/privateKey.pem` + `publicKey.pem`
- Admin credentials configured via `app.admin.username` / `app.admin.password` (env: `ADMIN_USERNAME`/`ADMIN_PASSWORD`, default: `admin`/`admin123`)
- Token duration: 3600s (configurable via `app.jwt.duration`)

## Database

PostgreSQL on `localhost:5432/online-cv-db`. Schema is `drop-and-create` in dev profile. Connection via env vars `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`.

**CORS**: Allowed origin is `http://localhost:9000` (frontend dev server).

## Testing

JUnit 5 + REST Assured + Mockito. Test HTTP port is randomized (`quarkus.http.test-port=0`). Testcontainers enabled for PostgreSQL in test profile.

- **Unit tests** (Mockito): `WorkExperienceServiceTest`, `AcademicStudyServiceTest`, `GenericInfoServiceTest`
- **Integration tests** (`@QuarkusTest` + REST Assured): `WorkExperienceResourceTest`, `AcademicStudyResourceTest`, `GenericInfoResourceTest`, `AuthResourceTest`, `CurriculumVitaeResourceTest`
- **Auth tests** (`@QuarkusTest` + `@InjectMock`): `AuthServiceTest`

### Coding Standards
- **Java Streams API**: Prefer functional programming with streams for collections processing
- **Lombok**: Use annotations (`@Data`, `@Builder`, `@AllArgsConstructor`, etc.) to reduce boilerplate
- **Use Java 21 virtual threads**: On every new API request
- **Try to prioritize Java records instead of Lombok**
- **Namings**: All variables uses descriptive names and always in english, and classes also in english always.
- **Readability**: Try to make code readable and follow KISS principles.
- **coding principles**: SOLID principles and Hexagonal Architecture best practices are followed.
