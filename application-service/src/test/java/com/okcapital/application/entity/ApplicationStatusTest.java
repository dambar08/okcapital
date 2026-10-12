package com.okcapital.application.entity;

import com.okcapital.application.dto.ApplicationStateResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationStatusTest {

    @Test
    void allowsOnlyConfiguredLifecycleTransitions() {
        assertTrue(ApplicationStatus.DRAFT.canTransitionTo(ApplicationStatus.SUBMITTED));
        assertTrue(ApplicationStatus.IN_PROGRESS.canTransitionTo(ApplicationStatus.UNDERWRITING));
        assertTrue(ApplicationStatus.FUNDING_IN_PROGRESS.canTransitionTo(ApplicationStatus.FUNDING_FAILED));
        assertFalse(ApplicationStatus.DRAFT.canTransitionTo(ApplicationStatus.FUNDED));
        assertFalse(ApplicationStatus.FUNDED.canTransitionTo(ApplicationStatus.FUNDING_IN_PROGRESS));
    }

    @Test
    void exposesTerminalStatesWithoutAllowedTransitions() {
        assertTrue(ApplicationStatus.FUNDED.getAllowedTransitions().isEmpty());
        assertTrue(ApplicationStatus.FUNDING_FAILED.getAllowedTransitions().isEmpty());
        assertTrue(ApplicationStatus.EXPIRED.getAllowedTransitions().isEmpty());
    }

    @Test
    void stateResponseContainsUiMetadataAndTransitions() {
        ApplicationStateResponse state = ApplicationStateResponse.from(ApplicationStatus.DRAFT);

        assertEquals(ApplicationStatus.DRAFT, state.key());
        assertEquals("Resume Application", state.displayLabel());
        assertEquals("User started entering data but hasn't finalized submission.", state.description());
        assertTrue(state.allowedTransitions().contains(ApplicationStatus.SUBMITTED));
    }

    @Test
    void mapsPreviousPersistedStatusesToNewLifecycleStates() {
        ApplicationStatusConverter converter = new ApplicationStatusConverter();

        assertEquals(ApplicationStatus.UNDERWRITING, converter.convertToEntityAttribute("UNDER_REVIEW"));
        assertEquals(ApplicationStatus.APPROVED_FINAL, converter.convertToEntityAttribute("APPROVED"));
        assertEquals(ApplicationStatus.DECLINED, converter.convertToEntityAttribute("REJECTED"));
        assertEquals(ApplicationStatus.INFO_REQUESTED, converter.convertToEntityAttribute("NEEDS_INFORMATION"));
        assertEquals(ApplicationStatus.WITHDRAWN, converter.convertToEntityAttribute("CANCELLED"));
    }
}
