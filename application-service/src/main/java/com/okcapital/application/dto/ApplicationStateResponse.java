package com.okcapital.application.dto;

import com.okcapital.application.entity.ApplicationStatus;

import java.util.Set;

public record ApplicationStateResponse(
        ApplicationStatus key,
        String displayLabel,
        String description,
        Set<ApplicationStatus> allowedTransitions) {

    public static ApplicationStateResponse from(ApplicationStatus status) {
        return new ApplicationStateResponse(
                status,
                status.getDisplayLabel(),
                status.getDescription(),
                status.getAllowedTransitions());
    }
}
