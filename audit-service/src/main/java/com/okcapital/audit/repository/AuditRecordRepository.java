package com.okcapital.audit.repository;

import com.okcapital.audit.entity.AuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AuditRecordRepository extends JpaRepository<AuditRecord, UUID>,
        JpaSpecificationExecutor<AuditRecord> {
    Optional<AuditRecord> findByEventId(UUID eventId);
}
