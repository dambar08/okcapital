package com.okcapital.payment.dto;

import java.math.BigDecimal;

public record CreatePaymentRequest(
                String loanId,
                String accountId,
                String paymentMethod,
                BigDecimal amount,
                String description) {
}
