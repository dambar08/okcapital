package com.okcapital.approval.repository;

import com.okcapital.approval.entity.ApprovalDecision;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<ApprovalDecision, java.util.UUID> {
}
