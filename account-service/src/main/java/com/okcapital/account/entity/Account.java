package com.okcapital.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.okcapital.shared.IdentifiableEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@AttributeOverride(name = "id", column = @Column(name = "account_id"))
public class Account extends IdentifiableEntity {
        @Column(nullable = false)
        private String customerId;
        @Column(nullable = false)
        private String accountType;
        @Column(nullable = false, length = 3)
        private String currency;
        @Column(nullable = false, precision = 19, scale = 4)
        private BigDecimal balance;
        @Column(nullable = false)
        private String status;

        protected Account() {
        }

        public Account(
                        UUID accountId,
                        String customerId,
                        String accountType,
                        String currency,
                        BigDecimal balance,
                        String status) {
                super(accountId);
                this.customerId = customerId;
                this.accountType = accountType;
                this.currency = currency;
                this.balance = balance;
                this.status = status;
        }

        public UUID accountId() {
                return id();
        }

        public String customerId() {
                return customerId;
        }

        public String accountType() {
                return accountType;
        }

        public String currency() {
                return currency;
        }

        public BigDecimal balance() {
                return balance;
        }

        public String status() {
                return status;
        }

        public UUID getAccountId() {
                return getId();
        }

        public String getCustomerId() {
                return customerId;
        }

        public String getAccountType() {
                return accountType;
        }

        public String getCurrency() {
                return currency;
        }

        public BigDecimal getBalance() {
                return balance;
        }

        public String getStatus() {
                return status;
        }
}
