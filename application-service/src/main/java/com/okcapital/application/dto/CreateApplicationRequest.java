package com.okcapital.application.dto;

public record CreateApplicationRequest(
        String customerId,
        String productId,
        double requestedAmount,
        int termMonths
) {
    public CreateApplicationRequest {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId is required");
        }
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId is required");
        }
        if (requestedAmount <= 0) {
            throw new IllegalArgumentException("requestedAmount must be positive");
        }
        if (termMonths <= 0) {
            throw new IllegalArgumentException("termMonths must be positive");
        }
    }
}
