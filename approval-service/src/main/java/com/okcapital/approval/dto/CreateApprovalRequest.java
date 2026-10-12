package com.okcapital.approval.dto;

import java.math.BigDecimal;

public record CreateApprovalRequest(
                String applicationId,
                String customerId,
                String status,
                BigDecimal approvedAmount,
                String notes) {
}
