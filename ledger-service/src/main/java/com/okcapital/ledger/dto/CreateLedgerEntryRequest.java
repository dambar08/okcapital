package com.okcapital.ledger.dto;

import java.math.BigDecimal;

public record CreateLedgerEntryRequest(
                String accountId,
                String type,
                BigDecimal amount,
                String description,
                String reference) {
}
