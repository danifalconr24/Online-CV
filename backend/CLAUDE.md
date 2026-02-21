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

**Stack**: Quarkus 3.5.3, Java 21, PostgreSQL, Hibernate ORM Panache, RESTEasy Reactive

**Architecture**: Hexagonal (ports & adapters) with three layers:

**Package structure** (`src/main/java/es/danifalconr/`):
- `domain/model/` — Pure POJOs (AcademicStudy, GenericInfo, WorkExperience, Language)
- `domain/port/out/` — Repository interfaces (out ports) defining persistence contracts
- `application/` — Use case services (`@ApplicationScoped`, `@Transactional`). Inject domain port interfaces.
- `infrastructure/rest/` — Thin REST adapters (JAX-RS endpoints). No `@Transactional`. Inject application services.
- `infrastructure/rest/dto/` — Java records for request/response DTOs
- `infrastructure/persistence/entity/` — JPA entities extending `PanacheEntity` with `@Table` annotations
- `infrastructure/persistence/mapper/` — Static mapper classes (`toDomain()`/`toEntity()`)
- `infrastructure/persistence/adapter/` — `@ApplicationScoped` Panache repository classes implementing domain port interfaces + `PanacheRepository<Entity>`
- `infrastructure/config/` — `SnakeCasePhysicalNamingStrategy` + `Bootstrap` (seeds initial data on startup)

**Dependency flow**: REST resources → Application services → Domain ports ← Persistence adapters

**API base path**: `/v1/curriculum-vitae/` with sub-paths: `generic-info`, `work-experiences`, `academic-studies`

**Database**: PostgreSQL on `localhost:5432/online-cv-db`. Connection configured via env vars `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`. Schema is `drop-and-create` on startup.

**CORS**: Allowed origin is `http://localhost:9000` (frontend dev server).

**Testing**: JUnit 5 + REST Assured + Mockito. Test HTTP port is randomized (`quarkus.http.test-port=0`).

### Coding Standards
- **Java Streams API**: Prefer functional programming with streams for collections processing
- **Lombok**: Use annotations (`@Data`, `@Builder`, `@AllArgsConstructor`, etc.) to reduce boilerplate
- **Use Java 21 virtual threads**: On every new API request
- **Try to prioritize Java records instead of Lombok**
- **Namings**: All variables uses descriptive names and always in english, and classes also in english always.
- **Readability**: Try to make code readable and follow KISS principles.