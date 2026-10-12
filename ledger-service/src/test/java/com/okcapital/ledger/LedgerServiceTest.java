package com.okcapital.ledger;

import com.okcapital.ledger.dto.CreateLedgerEntryRequest;
import com.okcapital.ledger.entity.LedgerEntry;
import com.okcapital.ledger.repository.LedgerRepository;
import com.okcapital.ledger.service.LedgerService;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LedgerServiceTest {

    @Test
    void createEntryStoresLedgerTransaction() {
        LedgerRepository repository = mock(LedgerRepository.class);
        when(repository.save(any(LedgerEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));
        LedgerService service = new LedgerService(repository);

        LedgerEntry entry = service.createEntry(new CreateLedgerEntryRequest(
                "acc-1001",
                "CREDIT",
                new BigDecimal("1250.00"),
                "Loan disbursement",
                "DISB-2025-001"));

        assertNotNull(entry.entryId());
        assertEquals(8, entry.entryId().version());
        assertEquals(2, entry.entryId().variant());
        assertEquals("acc-1001", entry.accountId());
        assertEquals("CREDIT", entry.type());
        assertEquals(new BigDecimal("1250.00"), entry.amount());
    }
}
