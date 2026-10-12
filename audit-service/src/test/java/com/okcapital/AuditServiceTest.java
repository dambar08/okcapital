package com.okcapital.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okcapital.audit.dto.AuditEvent;
import com.okcapital.audit.dto.AuditRecordResponse;
import com.okcapital.audit.entity.AuditRecord;
import com.okcapital.audit.repository.AuditRecordRepository;
import com.okcapital.audit.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuditServiceTest {
    private final AuditRecordRepository repository = mock(AuditRecordRepository.class);
    private final AuditService auditService = new AuditService(repository, new ObjectMapper());
    private final UUID eventId = UUID.fromString("00000000-0000-8000-8000-000000000001");
    private final Instant occurredAt = Instant.parse("2026-10-11T05:00:00Z");
    private AuditEvent event;

    @BeforeEach
    void setUp() throws Exception {
        event = new AuditEvent(
                eventId,
                "payment.confirmed",
                1,
                occurredAt,
                "payment-service",
                "correlation-1",
                "payment-1",
                new ObjectMapper().readTree("{\"amount\":42}")
        );
    }

    @Test
    void recordsEventAndReturnsStructuredPayload() {
        when(repository.findByEventId(eventId)).thenReturn(Optional.empty());
        when(repository.save(any(AuditRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuditRecordResponse response = auditService.record(event);

        assertEquals(eventId, response.eventId());
        assertEquals("payment.confirmed", response.eventType());
        assertEquals(42, response.payload().get("amount").intValue());
        assertNotNull(response.recordId());
        verify(repository).save(any(AuditRecord.class));
    }

    @Test
    void duplicateEventIdReturnsExistingRecordWithoutSavingAgain() {
        AuditRecord existing = new AuditRecord(
                UUID.fromString("00000000-0000-8000-8000-000000000002"),
                eventId,
                "payment.confirmed",
                1,
                occurredAt,
                "payment-service",
                "correlation-1",
                "payment-1",
                "{\"amount\":42}"
        );
        when(repository.findByEventId(eventId)).thenReturn(Optional.of(existing));

        AuditRecordResponse response = auditService.record(event);

        assertEquals(existing.getRecordId(), response.recordId());
        verify(repository, never()).save(any(AuditRecord.class));
    }

    @Test
    void rejectsAnEventIdReusedWithDifferentContent() {
        AuditRecord existing = new AuditRecord(
                UUID.fromString("00000000-0000-8000-8000-000000000002"),
                eventId,
                "payment.confirmed",
                1,
                occurredAt,
                "payment-service",
                "correlation-1",
                "payment-1",
                "{\"amount\":42}"
        );
        when(repository.findByEventId(eventId)).thenReturn(Optional.of(existing));
        AuditEvent conflictingEvent = new AuditEvent(
                eventId,
                "payment.confirmed",
                1,
                occurredAt,
                "payment-service",
                "correlation-1",
                "payment-1",
                new ObjectMapper().createObjectNode().put("amount", 43)
        );

        assertThrows(IllegalStateException.class, () -> auditService.record(conflictingEvent));
        verify(repository, never()).save(any(AuditRecord.class));
    }

    @Test
    void searchesAndMapsRecordsToResponseDtos() {
        AuditRecord record = new AuditRecord(
                UUID.fromString("00000000-0000-8000-8000-000000000002"),
                eventId,
                "payment.confirmed",
                1,
                occurredAt,
                "payment-service",
                "correlation-1",
                "payment-1",
                "{\"amount\":42}"
        );
        PageRequest pageable = PageRequest.of(0, 25, Sort.by(Sort.Direction.DESC, "occurredAt"));
        when(repository.findAll(
                org.mockito.ArgumentMatchers.<Specification<AuditRecord>>any(),
                eq(pageable)
        ))
                .thenReturn(new PageImpl<>(List.of(record), pageable, 1));

        var results = auditService.search(
                eventId,
                "payment.confirmed",
                "payment-service",
                "correlation-1",
                "payment-1",
                occurredAt,
                occurredAt,
                pageable
        );

        assertEquals(1, results.getTotalElements());
        assertEquals(eventId, results.getContent().getFirst().eventId());
        assertEquals(42, results.getContent().getFirst().payload().get("amount").intValue());
    }
}
