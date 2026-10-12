package com.okcapital.collections.service;

import com.okcapital.collections.dto.CreateCollectionsCaseRequest;
import com.okcapital.collections.entity.CollectionsCase;
import com.okcapital.collections.repository.CollectionsRepository;

import com.okcapital.shared.UuidV8Generator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class CollectionsService {

    private final CollectionsRepository repository;

    public CollectionsService(CollectionsRepository repository) {
        this.repository = repository;
    }

    public CollectionsCase createCase(CreateCollectionsCaseRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }
        if (request.loanId() == null || request.loanId().isBlank()) {
            throw new IllegalArgumentException("loanId must not be blank");
        }
        if (request.accountId() == null || request.accountId().isBlank()) {
            throw new IllegalArgumentException("accountId must not be blank");
        }
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        }

        CollectionsCase collectionsCase = new CollectionsCase(
                UuidV8Generator.generate(),
                request.loanId(),
                request.accountId(),
                request.status() == null || request.status().isBlank() ? "OPEN" : request.status(),
                request.amount(),
                request.notes() == null ? "Collections follow-up" : request.notes(),
                Instant.now());
        return repository.save(collectionsCase);
    }

    public List<CollectionsCase> findByLoanId(String loanId) {
        return repository.findByLoanId(loanId);
    }
}
