package com.okcapital.ledger.service;

import com.okcapital.ledger.dto.CreateLedgerEntryRequest;
import com.okcapital.ledger.entity.LedgerEntry;
import com.okcapital.ledger.repository.LedgerRepository;

import com.okcapital.shared.UuidV8Generator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LedgerService {
    private final LedgerRepository ledgerRepository;

    public LedgerService(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    public LedgerEntry createEntry(CreateLedgerEntryRequest request) {
        LedgerEntry entry = new LedgerEntry(
                UuidV8Generator.generate(),
                request.accountId(),
                request.type(),
                request.amount(),
                request.description(),
                request.reference());
        return ledgerRepository.save(entry);
    }

    public List<LedgerEntry> findAll() {
        return ledgerRepository.findAll();
    }
}
