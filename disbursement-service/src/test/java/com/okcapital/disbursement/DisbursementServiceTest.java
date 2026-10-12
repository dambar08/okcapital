package com.okcapital.disbursement;

import com.okcapital.disbursement.dto.CreateDisbursementRequest;
import com.okcapital.disbursement.entity.DisbursementRecord;
import com.okcapital.disbursement.repository.DisbursementRepository;
import com.okcapital.disbursement.service.DisbursementService;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DisbursementServiceTest {

    @Test
    void scheduleDisbursementCreatesRecord() {
        DisbursementRepository repository = mock(DisbursementRepository.class);
        when(repository.save(any(DisbursementRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));
        DisbursementService service = new DisbursementService(repository);

        DisbursementRecord record = service.scheduleDisbursement(new CreateDisbursementRequest(
                "loan-2001",
                "acct-3001",
                new BigDecimal("42000.00"),
                "Initial loan disbursement"));

        assertNotNull(record.disbursementId());
        assertEquals(record.disbursementId(), record.id());
        assertEquals("loan-2001", record.loanId());
        assertEquals("acct-3001", record.accountId());
        assertEquals(0, record.amount().compareTo(new BigDecimal("42000.00")));
    }
}
