package com.okcapital.credit.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record CreditPullResponse(
        UUID applicationId,
        String provider,
        JsonNode report
) {
}
