package com.okcapital.shared;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IdentifiableEntityTest {

    @Test
    void persistenceInitializesUuidAndAuditTimestamps() {
        TestEntity entity = new TestEntity();

        entity.persist();

        assertNotNull(entity.id());
        assertNotNull(entity.createdAt());
        assertEquals(entity.createdAt(), entity.updatedAt());
    }

    @Test
    void updateRefreshesUpdatedAtWithoutChangingCreatedAt() {
        TestEntity entity = new TestEntity();
        entity.persist();
        Instant createdAt = entity.createdAt();
        Instant firstUpdatedAt = entity.updatedAt();

        entity.update();

        assertEquals(createdAt, entity.createdAt());
        assertFalse(entity.updatedAt().isBefore(firstUpdatedAt));
    }

    @Test
    void constructorPreservesProvidedUuidAndCreatedAt() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-01-01T00:00:00Z");
        TestEntity entity = new TestEntity(id, createdAt);

        assertEquals(id, entity.id());
        assertEquals(createdAt, entity.createdAt());
    }

    private static class TestEntity extends IdentifiableEntity {

        TestEntity() {
        }

        TestEntity(UUID id, Instant createdAt) {
            super(id, createdAt);
        }

        void persist() {
            initializeId();
            initializeAuditTimestamps();
        }

        void update() {
            updateAuditTimestamp();
        }
    }
}
