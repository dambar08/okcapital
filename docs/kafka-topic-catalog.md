# Kafka event catalog

| Topic | Producer | Consumer | Key | Purpose |
| --- | --- | --- | --- | --- |
| application.submitted | application-service | fraud-service, credit-service | applicationId | Application enters review |
| application.updated | application-service | approval-service | applicationId | Application changes |
| document.verified | document-service | application-service | applicationId | Document validation done |
| credit.assessment.completed | credit-service | approval-service | applicationId | Affordability scores |
| approval.completed | approval-service | product-service, notification-service | applicationId | Final lending decision |
| offer.accepted | product-service | disbursement-service | offerId | Offer accepted |
| disbursement.requested | disbursement-service | payment-service | loanAccountId | Payout request |
| disbursement.completed | disbursement-service | account-service, ledger-service | loanAccountId | Funds settled |
| payment.initiated | payment-service | repayment-service | paymentId | Collection started |
| payment.confirmed | payment-service | repayment-service, ledger-service | paymentId | Payment confirmed |
| repayment.allocated | repayment-service | ledger-service | paymentId | Payment split across ledger |
| repayment.overdue | repayment-service | collections-service, notification-service | loanAccountId | Delinquency signal |
| fraud.flagged | fraud-service | approval-service | customerId | Suspicious signal |
| notification.requested | application-service, repayment-service | notification-service | correlationId | Notification trigger |
| audit.record.requested | all services | audit-service | correlationId | Event trail request |

## Event envelope

Every event includes eventId, eventType, eventVersion, occurredAt, producer, correlationId, aggregateId, and payload.

The audit service consumes `audit.record.requested` and stores the complete envelope. Repeated deliveries with the same
eventId return the existing audit record instead of creating duplicates. Authenticated readers can search
`GET /api/audit/records` by eventId, eventType, producer, correlationId, aggregateId, and inclusive occurredFrom /
occurredTo timestamps. Results are paginated (50 records by default, up to 100) and ordered newest first.
