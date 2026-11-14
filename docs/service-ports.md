# Service ports

These are the default local development ports. Each application has a unique
HTTP port. Override an application's port with the `SERVER_PORT` environment
variable when needed.

## Application services

| Service | Port | Local URL |
| --- | ---: | --- |
| API Gateway | 8080 | http://localhost:8080 |
| Auth Service | 8081 | http://localhost:8081 |
| Customer Service | 8082 | http://localhost:8082 |
| Application Service | 8083 | http://localhost:8083 |
| Document Service | 8084 | http://localhost:8084 |
| Credit Service | 8085 | http://localhost:8085 |
| Approval Service | 8086 | http://localhost:8086 |
| Product Service | 8087 | http://localhost:8087 |
| Disbursement Service | 8088 | http://localhost:8088 |
| Account Service | 8089 | http://localhost:8089 |
| Repayment Service | 8090 | http://localhost:8090 |
| Notification Service | 8091 | http://localhost:8091 |
| Payment Service | 8092 | http://localhost:8092 |
| Ledger Service | 8093 | http://localhost:8093 |
| Audit Service | 8094 | http://localhost:8094 |
| Fraud Service | 8095 | http://localhost:8095 |
| Collections Service | 8096 | http://localhost:8096 |
| Reporting Service | 8097 | http://localhost:8097 |
| Discovery (Eureka) | 8761 | http://localhost:8761 |

Swagger UI and OpenAPI endpoints are available for application services when
enabled by the active profile:

- Swagger UI: `http://localhost:<service-port>/swagger-ui/index.html`
- OpenAPI: `http://localhost:<service-port>/v3/api-docs`

## Local infrastructure

| Component | Port(s) |
| --- | ---: |
| PostgreSQL | 5432 |
| Kafka | 9092 |
| Keycloak HTTP / HTTPS | 9080 / 9443 |
| Redis | 6379 |
| Memcached | 11211 |
