package com.okcapital.collections.entity;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.okcapital.shared.IdentifiableEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "collections_cases")
@AttributeOverride(name = "id", column = @Column(name = "case_id"))
public class CollectionsCase extends IdentifiableEntity {
        @Column(nullable = false)
        private String loanId;
        @Column(nullable = false)
        private String accountId;
        @Column(nullable = false)
        private String status;
        @Column(nullable = false, precision = 19, scale = 4)
        private BigDecimal amount;
        private String notes;

        protected CollectionsCase() {
        }

        public CollectionsCase(UUID caseId, String loanId, String accountId, String status,
                        BigDecimal amount, String notes, Instant createdAt) {
                super(caseId, createdAt);
                this.loanId = loanId;
                this.accountId = accountId;
                this.status = status;
                this.amount = amount;
                this.notes = notes;
        }

        public UUID caseId() {
                return id();
        }

        public String loanId() {
                return loanId;
        }

        public String accountId() {
                return accountId;
        }

        public String status() {
                return status;
        }

        public BigDecimal amount() {
                return amount;
        }

        public String notes() {
                return notes;
        }

        public UUID getCaseId() {
                return getId();
        }

        public String getLoanId() {
                return loanId;
        }

        public String getAccountId() {
                return accountId;
        }

        public String getStatus() {
                return status;
        }

        public BigDecimal getAmount() {
                return amount;
        }

        public String getNotes() {
                return notes;
        }

}
