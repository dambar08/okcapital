package com.okcapital.audit;

import com.okcapital.audit.dto.AuditRecordResponse;
import com.okcapital.audit.service.AuditService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit/records")
@Validated
public class AuditRecordController {
    private final AuditService auditService;

    public AuditRecordController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public Page<AuditRecordResponse> search(
            @RequestParam(required = false) UUID eventId,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) String correlationId,
            @RequestParam(required = false) String aggregateId,
            @RequestParam(required = false) Instant occurredFrom,
            @RequestParam(required = false) Instant occurredTo,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "50") @Min(1) @Max(100) int size
    ) {
        if (occurredFrom != null && occurredTo != null && occurredFrom.isAfter(occurredTo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "occurredFrom must not be after occurredTo");
        }

        return auditService.search(
                eventId,
                eventType,
                producer,
                correlationId,
                aggregateId,
                occurredFrom,
                occurredTo,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"))
        );
    }
}
