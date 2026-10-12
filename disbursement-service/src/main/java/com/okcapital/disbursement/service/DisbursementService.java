package com.okcapital.disbursement.service;

import com.okcapital.disbursement.dto.CreateDisbursementRequest;
import com.okcapital.disbursement.entity.DisbursementRecord;
import com.okcapital.disbursement.repository.DisbursementRepository;

import com.okcapital.shared.UuidV8Generator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DisbursementService {

    private final DisbursementRepository repository;

    public DisbursementService(DisbursementRepository repository) {
        this.repository = repository;
    }

    public DisbursementRecord scheduleDisbursement(CreateDisbursementRequest request) {
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

        DisbursementRecord record = new DisbursementRecord(
                UuidV8Generator.generate(),
                request.loanId(),
                request.accountId(),
                request.amount(),
                request.description() == null ? "Disbursement" : request.description(),
                "SCHEDULED");
        return repository.save(record);
    }

    public List<DisbursementRecord> findByLoanId(String loanId) {
        return repository.findByLoanId(loanId);
    }
}
