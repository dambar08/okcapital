package com.okcapital.audit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okcapital.audit.dto.AuditEvent;
import com.okcapital.audit.dto.AuditRecordResponse;
import com.okcapital.audit.entity.AuditRecord;
import com.okcapital.audit.repository.AuditRecordRepository;
import com.okcapital.shared.UuidV8Generator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuditService {
    private final AuditRecordRepository auditRecordRepository;
    private final ObjectMapper objectMapper;

    public AuditService(AuditRecordRepository auditRecordRepository, ObjectMapper objectMapper) {
        this.auditRecordRepository = auditRecordRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public AuditRecordResponse record(AuditEvent event) {
        Optional<AuditRecord> existingRecord = auditRecordRepository.findByEventId(event.eventId());
        AuditRecord record;
        if (existingRecord.isPresent()) {
            record = existingRecord.get();
            if (!matchesEvent(record, event)) {
                throw new IllegalStateException("Audit event id was reused with different content: " + event.eventId());
            }
        } else {
            record = auditRecordRepository.save(new AuditRecord(
                        UuidV8Generator.generate(),
                        event.eventId(),
                        event.eventType(),
                        event.eventVersion(),
                        event.occurredAt(),
                        event.producer(),
                        event.correlationId(),
                        event.aggregateId(),
                        event.payload().toString()
                ));
        }
        return toResponse(record);
    }

    @Transactional(readOnly = true)
    public Page<AuditRecordResponse> search(
            UUID eventId,
            String eventType,
            String producer,
            String correlationId,
            String aggregateId,
            Instant occurredFrom,
            Instant occurredTo,
            Pageable pageable
    ) {
        Specification<AuditRecord> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (eventId != null) {
                predicates.add(criteriaBuilder.equal(root.get("eventId"), eventId));
            }
            if (hasText(eventType)) {
                predicates.add(criteriaBuilder.equal(root.get("eventType"), eventType));
            }
            if (hasText(producer)) {
                predicates.add(criteriaBuilder.equal(root.get("producer"), producer));
            }
            if (hasText(correlationId)) {
                predicates.add(criteriaBuilder.equal(root.get("correlationId"), correlationId));
            }
            if (hasText(aggregateId)) {
                predicates.add(criteriaBuilder.equal(root.get("aggregateId"), aggregateId));
            }
            if (occurredFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("occurredAt"), occurredFrom));
            }
            if (occurredTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("occurredAt"), occurredTo));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };

        return auditRecordRepository.findAll(specification, pageable).map(this::toResponse);
    }

    private AuditRecordResponse toResponse(AuditRecord record) {
        return new AuditRecordResponse(
                record.getRecordId(),
                record.getEventId(),
                record.getEventType(),
                record.getEventVersion(),
                record.getOccurredAt(),
                record.getProducer(),
                record.getCorrelationId(),
                record.getAggregateId(),
                readPayload(record),
                record.getCreatedAt()
        );
    }

    private boolean matchesEvent(AuditRecord record, AuditEvent event) {
        return record.getEventType().equals(event.eventType())
                && record.getEventVersion() == event.eventVersion()
                && record.getOccurredAt().equals(event.occurredAt())
                && record.getProducer().equals(event.producer())
                && record.getCorrelationId().equals(event.correlationId())
                && record.getAggregateId().equals(event.aggregateId())
                && readPayload(record).equals(event.payload());
    }

    private JsonNode readPayload(AuditRecord record) {
        try {
            return objectMapper.readTree(record.getPayload());
        } catch (java.io.IOException exception) {
            throw new IllegalStateException("Stored audit record payload is not valid JSON", exception);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
