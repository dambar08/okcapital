package com.okcapital.audit.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record AuditEvent(
        @NotNull UUID eventId,
        @NotBlank @Size(max = 200) String eventType,
        @NotNull @Positive Integer eventVersion,
        @NotNull Instant occurredAt,
        @NotBlank @Size(max = 100) String producer,
        @NotBlank @Size(max = 200) String correlationId,
        @NotBlank @Size(max = 200) String aggregateId,
        @NotNull JsonNode payload
) {
}
