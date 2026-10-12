package com.okcapital.audit;

import com.okcapital.audit.dto.AuditEvent;
import com.okcapital.audit.service.AuditService;
import jakarta.validation.Valid;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class AuditEventListener {
    private final AuditService auditService;

    public AuditEventListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(
            topics = "${app.kafka.audit-topic:audit.record.requested}",
            groupId = "${spring.kafka.consumer.group-id:audit-service}"
    )
    public void onAuditRecordRequested(@Valid AuditEvent event) {
        auditService.record(event);
    }
}
