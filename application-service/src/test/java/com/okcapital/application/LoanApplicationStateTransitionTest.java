package com.okcapital.application;

import com.okcapital.application.entity.ApplicationStatus;
import com.okcapital.application.entity.InvalidApplicationTransitionException;
import com.okcapital.application.entity.LoanApplication;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoanApplicationStateTransitionTest {

    @Test
    void transitionsToAnAllowedState() {
        LoanApplication application = createApplication(ApplicationStatus.DRAFT);

        application.transitionTo(ApplicationStatus.SUBMITTED);

        assertEquals(ApplicationStatus.SUBMITTED, application.getStatus());
    }

    @Test
    void rejectsAnUnlistedTransition() {
        LoanApplication application = createApplication(ApplicationStatus.DRAFT);

        assertThrows(
                InvalidApplicationTransitionException.class,
                () -> application.transitionTo(ApplicationStatus.FUNDED));
        assertEquals(ApplicationStatus.DRAFT, application.getStatus());
    }

    private LoanApplication createApplication(ApplicationStatus status) {
        return new LoanApplication(UUID.randomUUID(), "customer-1", "product-1", 1000, 12, status);
    }
}
