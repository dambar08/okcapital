package com.okcapital.product.dto;

public record CreateProductRequest(
        String code,
        String name,
        double minAmount,
        double maxAmount,
        int minTenureMonths,
        int maxTenureMonths,
        double interestRate,
        double processingFee,
        int minCreditScore
) {
    public CreateProductRequest {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (maxAmount < minAmount) {
            throw new IllegalArgumentException("maxAmount must be greater than or equal to minAmount");
        }
    }
}
