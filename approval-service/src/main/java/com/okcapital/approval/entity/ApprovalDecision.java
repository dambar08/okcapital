package com.okcapital.approval.entity;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.okcapital.shared.IdentifiableEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "approval_decisions")
@AttributeOverride(name = "id", column = @Column(name = "decision_id"))
public class ApprovalDecision extends IdentifiableEntity {
        @Column(nullable = false)
        private String applicationId;
        @Column(nullable = false)
        private String customerId;
        @Column(nullable = false)
        private String status;
        @Column(precision = 19, scale = 4)
        private BigDecimal approvedAmount;
        private String notes;

        protected ApprovalDecision() {
        }

        public ApprovalDecision(UUID decisionId, String applicationId, String customerId, String status,
                        BigDecimal approvedAmount, String notes) {
                super(decisionId);
                this.applicationId = applicationId;
                this.customerId = customerId;
                this.status = status;
                this.approvedAmount = approvedAmount;
                this.notes = notes;
        }

        public UUID decisionId() {
                return id();
        }

        public String applicationId() {
                return applicationId;
        }

        public String customerId() {
                return customerId;
        }

        public String status() {
                return status;
        }

        public BigDecimal approvedAmount() {
                return approvedAmount;
        }

        public String notes() {
                return notes;
        }

        public UUID getDecisionId() {
                return getId();
        }

        public String getApplicationId() {
                return applicationId;
        }

        public String getCustomerId() {
                return customerId;
        }

        public String getStatus() {
                return status;
        }

        public BigDecimal getApprovedAmount() {
                return approvedAmount;
        }

        public String getNotes() {
                return notes;
        }
}
