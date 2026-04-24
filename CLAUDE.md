# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean install

# Run (default dev profile)
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ResourceApplicationServiceTest

# Run a single test method
./mvnw test -Dtest=ResourceApplicationServiceTest#shouldCreateResource

# Generate JaCoCo coverage report (output: target/site/jacoco/jacoco.xml)
./mvnw verify
```

On Windows, use `.\mvnw.cmd` instead of `./mvnw`.

## Architecture

This project implements **Hexagonal Architecture (Ports & Adapters)**. The dependency rule flows inward: infrastructure → application → domain.

```
com/example/demo/
├── domain/           # Core business rules, no framework dependencies
│   ├── model/        # Resource record (immutable, self-validating)
│   └── validation/   # DomainValidationException
├── application/      # Use case orchestration
│   ├── port/in/      # Inbound use case interfaces + command objects
│   ├── port/out/     # Outbound repository port interfaces
│   ├── service/      # Application services implementing use case interfaces
│   └── exception/    # ApplicationException, ResourceNotFoundException
└── infrastructure/   # Frameworks, DB, HTTP — all adapters
    ├── adapter/in/web/          # REST controllers, request/response DTOs, web mappers
    ├── adapter/out/persistence/ # JPA entities, persistence mappers, repository adapters
    └── configuration/           # OpenAPI/Swagger config
```

**Request flow:** Controller → WebMapper → Command → ApplicationService → RepositoryPort → PersistenceAdapter → JPA

## Key Patterns

**Domain model is a Java record** (`Resource`) with a compact constructor that enforces all invariants (required fields, max lengths, default status `"ACTIVE"`). Never bypass this constructor.

**Use cases are interfaces** in `port/in/`. Each maps to one operation. Application services implement these interfaces and depend only on the outbound `ResourceRepositoryPort` interface — never on JPA directly.

**Mappers are explicit classes** (not MapStruct): `ResourceWebMapper` converts between DTOs and commands/domain objects; `ResourcePersistenceMapper` converts between domain model and JPA entity.

**Error envelope:** `GlobalExceptionHandler` catches `DomainValidationException`, `ResourceNotFoundException`, and `MethodArgumentNotValidException`, returning a consistent JSON structure:
```json
{ "errors": [{ "code": "...", "message": "...", "attribute": "...", "path": "..." }] }
```

## Profiles & Database

| Profile | Database | Notes |
|---------|----------|-------|
| `dev` (default) | H2 in-memory | H2 console at `/h2-console` |
| `test` | H2 in-memory (isolated) | Tracing disabled |
| `prod` | PostgreSQL | Requires `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` env vars |

Schema migrations are managed by **Liquibase** (`src/main/resources/db/changelog/`).

## Observability

- Actuator endpoints: `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`
- Distributed tracing via OpenTelemetry OTLP (configure with `OTEL_EXPORTER_OTLP_ENDPOINT`)
- Trace/span IDs injected into logs via `logback-spring.xml`
- Swagger UI: `/swagger-ui.html` | API docs: `/v3/api-docs`

## Testing

Unit tests mock the repository port with Mockito. Integration tests (`@SpringBootTest` + `@ActiveProfiles("test")`) use MockMvc against the full Spring context with an in-memory H2 database. Integration tests validate the full request/response cycle including the error envelope structure.
