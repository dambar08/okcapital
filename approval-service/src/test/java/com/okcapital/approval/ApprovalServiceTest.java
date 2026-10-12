package com.okcapital.approval;

import com.okcapital.approval.dto.CreateApprovalRequest;
import com.okcapital.approval.entity.ApprovalDecision;
import com.okcapital.approval.repository.ApprovalRepository;
import com.okcapital.approval.service.ApprovalService;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApprovalServiceTest {

    @Test
    void approveApplicationCreatesDecision() {
        ApprovalRepository repository = mock(ApprovalRepository.class);
        when(repository.save(any(ApprovalDecision.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ApprovalService service = new ApprovalService(repository);

        ApprovalDecision decision = service.approveApplication(new CreateApprovalRequest(
                "app-1001",
                "customer-1",
                "APPROVED",
                new BigDecimal("42000.00"),
                "Approved for full amount"));

        assertNotNull(decision.decisionId());
        assertEquals("app-1001", decision.applicationId());
        assertEquals("APPROVED", decision.status());
    }
}
