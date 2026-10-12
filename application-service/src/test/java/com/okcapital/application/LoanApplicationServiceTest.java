package com.okcapital.application;

import com.okcapital.application.dto.CreateApplicationRequest;
import com.okcapital.application.entity.LoanApplication;
import com.okcapital.application.repository.ApplicationRepository;
import com.okcapital.application.service.LoanApplicationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoanApplicationServiceTest {

    @Test
    void createApplicationStartsAsDraft() {
        ApplicationRepository repository = mock(ApplicationRepository.class);
        when(repository.save(any(LoanApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));
        LoanApplicationService service = new LoanApplicationService(repository);

        LoanApplication application = service.createApplication(new CreateApplicationRequest(
                "customer-123",
                "product-456",
                15000.00,
                24));

        assertNotNull(application.getApplicationId());
        assertEquals("customer-123", application.getCustomerId());
        assertEquals("DRAFT", application.getStatus().name());
    }
}
