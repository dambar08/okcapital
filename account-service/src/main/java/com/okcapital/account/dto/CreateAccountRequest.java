package com.okcapital.account.dto;

public record CreateAccountRequest(
                String customerId,
                String accountType,
                String currency) {
}
