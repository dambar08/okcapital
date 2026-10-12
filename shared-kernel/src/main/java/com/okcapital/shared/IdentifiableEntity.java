package com.okcapital.shared;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class IdentifiableEntity extends AuditedEntity {

    @Id
    private UUID id;

    protected IdentifiableEntity() {
    }

    protected IdentifiableEntity(UUID id) {
        this.id = id;
    }

    protected IdentifiableEntity(UUID id, Instant createdAt) {
        super(createdAt);
        this.id = id;
    }

    @PrePersist
    protected void initializeId() {
        if (id == null) {
            id = UuidV8Generator.generate();
        }
    }

    public UUID id() {
        return id;
    }

    public UUID getId() {
        return id;
    }
}
