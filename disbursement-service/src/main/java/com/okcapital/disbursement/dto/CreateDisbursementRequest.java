package com.okcapital.disbursement.dto;

import java.math.BigDecimal;

public record CreateDisbursementRequest(
                String loanId,
                String accountId,
                BigDecimal amount,
                String description) {
}
