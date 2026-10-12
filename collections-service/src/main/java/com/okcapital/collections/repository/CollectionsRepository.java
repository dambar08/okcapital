package com.okcapital.collections.repository;

import com.okcapital.collections.entity.CollectionsCase;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CollectionsRepository extends JpaRepository<CollectionsCase, java.util.UUID> {
    List<CollectionsCase> findByLoanId(String loanId);
}
