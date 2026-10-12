package com.okcapital.ledger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.okcapital.shared.IdentifiableEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
@AttributeOverride(name = "id", column = @Column(name = "entry_id"))
public class LedgerEntry extends IdentifiableEntity {
        @Column(nullable = false)
        private String accountId;
        @Column(nullable = false)
        private String type;
        @Column(nullable = false, precision = 19, scale = 4)
        private BigDecimal amount;
        private String description;
        private String reference;

        protected LedgerEntry() {
        }

        public LedgerEntry(
                        UUID entryId,
                        String accountId,
                        String type,
                        BigDecimal amount,
                        String description,
                        String reference) {
                super(entryId);
                this.accountId = accountId;
                this.type = type;
                this.amount = amount;
                this.description = description;
                this.reference = reference;
        }

        public UUID entryId() {
                return id();
        }

        public String accountId() {
                return accountId;
        }

        public String type() {
                return type;
        }

        public BigDecimal amount() {
                return amount;
        }

        public String description() {
                return description;
        }

        public String reference() {
                return reference;
        }

        public UUID getEntryId() {
                return getId();
        }

        public String getAccountId() {
                return accountId;
        }

        public String getType() {
                return type;
        }

        public BigDecimal getAmount() {
                return amount;
        }

        public String getDescription() {
                return description;
        }

        public String getReference() {
                return reference;
        }
}
