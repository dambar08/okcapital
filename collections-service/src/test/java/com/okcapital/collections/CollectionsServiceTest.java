package com.okcapital.collections;

import com.okcapital.collections.dto.CreateCollectionsCaseRequest;
import com.okcapital.collections.entity.CollectionsCase;
import com.okcapital.collections.repository.CollectionsRepository;
import com.okcapital.collections.service.CollectionsService;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CollectionsServiceTest {

    @Test
    void createCaseCreatesCollectionsRecord() {
        CollectionsRepository repository = mock(CollectionsRepository.class);
        when(repository.save(any(CollectionsCase.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CollectionsService service = new CollectionsService(repository);

        CollectionsCase result = service.createCase(new CreateCollectionsCaseRequest(
                "loan-2002",
                "acct-4002",
                "LATE_30",
                new BigDecimal("2500.00"),
                "Customer missed 30-day payment"));

        assertNotNull(result.caseId());
        assertEquals("loan-2002", result.loanId());
        assertEquals("acct-4002", result.accountId());
        assertEquals("LATE_30", result.status());
        assertEquals(0, result.amount().compareTo(new BigDecimal("2500.00")));
    }
}
