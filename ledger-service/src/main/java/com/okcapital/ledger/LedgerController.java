package com.okcapital.ledger;

import com.okcapital.ledger.dto.CreateLedgerEntryRequest;
import com.okcapital.ledger.entity.LedgerEntry;
import com.okcapital.ledger.service.LedgerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("/entries")
    public List<LedgerEntry> listEntries() {
        return ledgerService.findAll();
    }

    @PostMapping("/entries")
    public ResponseEntity<LedgerEntry> createEntry(@RequestBody CreateLedgerEntryRequest request) {
        LedgerEntry entry = ledgerService.createEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }
}
