package com.okcapital.document.dto;

import com.okcapital.document.entity.Document;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String applicationId,
        String customerId,
        String documentType,
        String fileName,
        String contentType,
        long sizeBytes,
        Instant createdAt
) {
    public static DocumentResponse from(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getApplicationId(),
                document.getCustomerId(),
                document.getDocumentType(),
                document.getFileName(),
                document.getContentType(),
                document.getSizeBytes(),
                document.getCreatedAt());
    }
}
