package com.okcapital.collections.dto;

import java.math.BigDecimal;

public record CreateCollectionsCaseRequest(
                String loanId,
                String accountId,
                String status,
                BigDecimal amount,
                String notes) {
}
