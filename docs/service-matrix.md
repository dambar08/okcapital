# Database ownership and service boundaries

| Service | Database ownership | Notes |
| --- | --- | --- |
| auth-service | user identities and access policies | Keycloak integration remains the system of record for credentials |
| customer-service | customers and addresses | personal data minimization |
| application-service | applications and history | owns workflow state |
| document-service | document metadata | files in MinIO |
| credit-service | assessments and credit pulls | Configurable TransUnion HTTP pull adapter; requires a contracted endpoint and bearer access token |
| approval-service | approvals and decisions | manual approval workflow |
| product-service | products and offers | accepted offer snapshot history |
| disbursement-service | payout instructions | idempotency critical |
| account-service | loan accounts | lifecycle ownership |
| repayment-service | schedules and installments | payment allocation |
| payment-service | payment attempts | webhooks and retries |
| ledger-service | journal entries | double-entry accounting |
| audit-service | audit records | restricted access |
| fraud-service | reviews and indicators | explainable rule outputs |
| collections-service | cases and reminders | delinquency workflows |

## Document service

`POST /api/documents` accepts multipart form data with `file` and `documentType`, and at least one of `applicationId` or `customerId`. It stores file bytes in MinIO and searchable metadata in the document database. `GET /api/documents` lists metadata and optionally filters by either association; `GET /api/documents/{documentId}` returns metadata, and `GET /api/documents/{documentId}/content` streams the document as an attachment.

MinIO connection settings are configurable with `MINIO_ENDPOINT`, `MINIO_ACCESS_KEY`, `MINIO_SECRET_KEY`, and `MINIO_DOCUMENT_BUCKET`. Uploads default to a 10 MB maximum; `DOCUMENT_MAX_FILE_SIZE` configures the limit.

## TransUnion credit pull

`POST /api/credit-reports/transunion/pulls` accepts an `applicationId`, applicant first/last name, date of birth, SSN, street address, city, state, and postal code. It sends the applicant fields as JSON to `TU_API_URL` with `Authorization: Bearer <TU_ACCESS_TOKEN>` and returns the provider's JSON report with the application ID. Provider error bodies are not returned because they may contain sensitive data.

The service requires a pre-issued bearer token and a product-specific endpoint. TransUnion products can have different authentication and request contracts; adapt the outbound mapping/authentication to the contracted product before connecting this endpoint to production.
| reporting-service | read-model tables | eventual consistency accepted |
