package com.okcapital.ledger.repository;

import com.okcapital.ledger.entity.LedgerEntry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerRepository extends JpaRepository<LedgerEntry, java.util.UUID> {
}
