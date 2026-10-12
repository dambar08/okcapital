package com.okcapital.audit.entity;

import com.okcapital.shared.IdentifiableEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "audit_records",
        indexes = {
                @Index(name = "idx_audit_records_occurred_at", columnList = "occurred_at"),
                @Index(name = "idx_audit_records_event_type_occurred_at", columnList = "event_type, occurred_at"),
                @Index(name = "idx_audit_records_correlation_id", columnList = "correlation_id"),
                @Index(name = "idx_audit_records_aggregate_id", columnList = "aggregate_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "record_id"))
public class AuditRecord extends IdentifiableEntity {
    @Column(nullable = false, unique = true, updatable = false)
    private UUID eventId;

    @Column(nullable = false, length = 200, updatable = false)
    private String eventType;

    @Column(nullable = false, updatable = false)
    private int eventVersion;

    @Column(nullable = false, updatable = false)
    private Instant occurredAt;

    @Column(nullable = false, length = 100, updatable = false)
    private String producer;

    @Column(nullable = false, length = 200, updatable = false)
    private String correlationId;

    @Column(nullable = false, length = 200, updatable = false)
    private String aggregateId;

    @Column(nullable = false, columnDefinition = "text", updatable = false)
    private String payload;

    protected AuditRecord() {
    }

    public AuditRecord(
            UUID recordId,
            UUID eventId,
            String eventType,
            int eventVersion,
            Instant occurredAt,
            String producer,
            String correlationId,
            String aggregateId,
            String payload
    ) {
        super(recordId);
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.occurredAt = occurredAt;
        this.producer = producer;
        this.correlationId = correlationId;
        this.aggregateId = aggregateId;
        this.payload = payload;
    }

    public UUID getRecordId() {
        return getId();
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public int getEventVersion() {
        return eventVersion;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getProducer() {
        return producer;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getPayload() {
        return payload;
    }
}
