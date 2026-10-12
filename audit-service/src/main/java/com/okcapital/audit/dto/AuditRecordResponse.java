package com.okcapital.audit.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record AuditRecordResponse(
        UUID recordId,
        UUID eventId,
        String eventType,
        int eventVersion,
        Instant occurredAt,
        String producer,
        String correlationId,
        String aggregateId,
        JsonNode payload,
        Instant receivedAt
) {
}
