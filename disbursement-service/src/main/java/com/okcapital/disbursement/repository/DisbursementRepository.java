package com.okcapital.disbursement.repository;

import com.okcapital.disbursement.entity.DisbursementRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DisbursementRepository extends JpaRepository<DisbursementRecord, java.util.UUID> {
    List<DisbursementRecord> findByLoanId(String loanId);
}
