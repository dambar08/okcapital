package com.okcapital.application.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ApplicationStatusConverter implements AttributeConverter<ApplicationStatus, String> {

    @Override
    public String convertToDatabaseColumn(ApplicationStatus status) {
        return status == null ? null : status.name();
    }

    @Override
    public ApplicationStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "UNDER_REVIEW" -> ApplicationStatus.UNDERWRITING;
            case "APPROVED" -> ApplicationStatus.APPROVED_FINAL;
            case "REJECTED" -> ApplicationStatus.DECLINED;
            case "NEEDS_INFORMATION" -> ApplicationStatus.INFO_REQUESTED;
            case "CANCELLED" -> ApplicationStatus.WITHDRAWN;
            default -> ApplicationStatus.valueOf(value);
        };
    }
}
