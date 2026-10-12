package com.okcapital.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.okcapital.shared.IdentifiableEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@AttributeOverride(name = "id", column = @Column(name = "payment_id"))
public class PaymentRecord extends IdentifiableEntity {
        @Column(nullable = false)
        private String loanId;
        @Column(nullable = false)
        private String accountId;
        @Column(nullable = false)
        private String paymentMethod;
        @Column(nullable = false, precision = 19, scale = 4)
        private BigDecimal amount;
        private String description;
        @Column(nullable = false)
        private String status;

        protected PaymentRecord() {
        }

        public PaymentRecord(UUID paymentId, String loanId, String accountId, String paymentMethod,
                        BigDecimal amount, String description, String status) {
                super(paymentId);
                this.loanId = loanId;
                this.accountId = accountId;
                this.paymentMethod = paymentMethod;
                this.amount = amount;
                this.description = description;
                this.status = status;
        }

        public UUID paymentId() {
                return id();
        }

        public String loanId() {
                return loanId;
        }

        public String accountId() {
                return accountId;
        }

        public String paymentMethod() {
                return paymentMethod;
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

        public UUID getPaymentId() {
                return getId();
        }

        public String getLoanId() {
                return loanId;
        }

        public String getAccountId() {
                return accountId;
        }

        public String getPaymentMethod() {
                return paymentMethod;
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
