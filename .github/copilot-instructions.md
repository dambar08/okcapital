# Copilot instructions for OkCapital

## Project overview

This repository is a multi-service Spring Boot platform for a digital lending system. The root Gradle build defines a set of independently buildable service modules, each with its own Spring Boot application and database boundary. The repo is intentionally scaffolded and staged: it is buildable and testable, but many business flows are still placeholders rather than full production logic.

Key facts from the repo:
- Java 25 is the Gradle toolchain.
- Spring Boot 3.5.6 and Spring Cloud 2025.x are the baseline.
- PostgreSQL 16, Kafka in KRaft mode, Keycloak, and MinIO are used for local development.
- The root includes service modules such as `auth-service`, `customer-service`, `application-service`, `product-service`, `ledger-service`, `reporting-service`, and the `api-gateway`.
- External infrastructure is defined in `docker-compose.yml` and the `infrastructure/` folder.

## Build, test, and verification commands

Use the wrapper from the repo root.

```bash
# Show the module graph
./gradlew projects

# Run the full repository test suite
./gradlew test

# Run one service's tests
./gradlew :auth-service:test
./gradlew :customer-service:test

# Run a single test class or pattern within a service
./gradlew :customer-service:test --tests '*Customer*Tests'
./gradlew :auth-service:test --tests 'com.okcapital.auth.*Auth*Test'

# Build a service jar
./gradlew :api-gateway:bootJar

# Build all application jars
./gradlew bootJar

# Start the local platform dependencies
docker compose up -d
```

Notes:
- There is no dedicated repository-wide lint task configured in the Gradle build files; the expected validation path is compilation + JUnit tests.
- The root build config sets `useJUnitPlatform()` for all Gradle tests.
- The repo includes a Gradle wrapper (`gradlew`), so prefer it over a locally installed Gradle.

## High-level architecture

The big picture is a domain-oriented microservice architecture with a shared event backbone and a central gateway:

- `api-gateway` sits at the edge and routes external traffic.
- `auth-service` handles identity and token validation via Keycloak/OAuth2.
- Domain services own their data and responsibilities, for example:
  - `customer-service` for customer and KYC state
  - `application-service` for loan applications and workflow
  - `credit-service` and `approval-service` for risk and underwriting
  - `product-service` for offers/pricing
  - `disbursement-service`, `payment-service`, and `ledger-service` for payouts and accounting
  - `audit-service`, `fraud-service`, `collections-service`, and `reporting-service` for supporting operational flows
- Messaging is Kafka-first for cross-service propagation; the docs describe an event-driven lifecycle across application, approval, payment, disbursement, reporting, and audit flows.
- PostgreSQL is the persistence boundary for each service; the architecture assumes data exchange through HTTP and Kafka rather than a shared database.
- External local dependencies are intentionally isolated behind `docker-compose.yml` and `infrastructure/` (Kafka, Postgres, Keycloak, MinIO).

When working across services, assume the system is intentionally modular: the right change often lives in a single service module, and cross-service behavior is coordinated through Kafka topics or HTTP contracts rather than hidden shared state.

## Key conventions and repository patterns

- Module-first layout: root build files orchestrate many `*-service` modules, and most development work should be done inside the relevant module rather than the root.
- Spring Boot standard layout: each service is a normal Spring Boot app with `src/main/java` and `src/test/java`; keep Java package naming aligned with the service's domain.
- JUnit 5 is the standard test runtime for all modules.
- Infrastructure code is deliberately separated from the application modules: use `docker-compose.yml` and `infrastructure/` for local platform dependencies, not service-local ad hoc config.
- This repo is intentionally staged: placeholder/mock integrations are expected for credit bureau, payment gateways, notification channels, and fraud providers. Do not assume fully implemented production integrations are present in every service.
- Keep cross-service coupling explicit: prefer documented contracts and async Kafka topics over direct database access across modules.
- Use `./gradlew` as the source of truth for build/test commands; avoid ad hoc shell scripts or custom local runners unless explicitly documented elsewhere.

## Practical guidance for this repo

- When adding functionality, start by identifying the owning service from the architecture docs and service map.
- If the work spans multiple services, check the Kafka topic catalog and service responsibilities before wiring new events or dependencies.
- Prefer updates that preserve the module boundary and local database ownership model.
- If you need to validate local infrastructure, use the docker compose environment rather than creating custom stand-ins.
