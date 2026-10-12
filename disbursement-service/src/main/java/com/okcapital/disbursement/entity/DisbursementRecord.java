package com.okcapital.disbursement.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.okcapital.shared.IdentifiableEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "disbursements")
@AttributeOverride(name = "id", column = @Column(name = "disbursement_id"))
public class DisbursementRecord extends IdentifiableEntity {
        @Column(nullable = false)
        private String loanId;
        @Column(nullable = false)
        private String accountId;
        @Column(nullable = false, precision = 19, scale = 4)
        private BigDecimal amount;
        private String description;
        @Column(nullable = false)
        private String status;

        protected DisbursementRecord() {
        }

        public DisbursementRecord(UUID disbursementId, String loanId, String accountId, BigDecimal amount,
                        String description, String status) {
                super(disbursementId);
                this.loanId = loanId;
                this.accountId = accountId;
                this.amount = amount;
                this.description = description;
                this.status = status;
        }

        public UUID disbursementId() {
                return id();
        }

        public String loanId() {
                return loanId;
        }

        public String accountId() {
                return accountId;
        }

        public BigDecimal amount() {
                return amount;
        }

        public String description() {
                return description;
        }

        public String status() {
                return status;
        }

        public UUID getDisbursementId() {
                return getId();
        }

        public String getLoanId() {
                return loanId;
        }

        public String getAccountId() {
                return accountId;
        }

        public BigDecimal getAmount() {
                return amount;
        }

        public String getDescription() {
                return description;
        }

        public String getStatus() {
                return status;
        }

}
