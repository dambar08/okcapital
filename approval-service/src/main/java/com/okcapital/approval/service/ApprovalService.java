package com.okcapital.approval.service;

import com.okcapital.approval.dto.CreateApprovalRequest;
import com.okcapital.approval.entity.ApprovalDecision;
import com.okcapital.approval.repository.ApprovalRepository;

import com.okcapital.shared.UuidV8Generator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalService {
    private final ApprovalRepository approvalRepository;

    public ApprovalService(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    public ApprovalDecision approveApplication(CreateApprovalRequest request) {
        ApprovalDecision decision = new ApprovalDecision(
                UuidV8Generator.generate(),
                request.applicationId(),
                request.customerId(),
                request.status(),
                request.approvedAmount(),
                request.notes());
        return approvalRepository.save(decision);
    }

    public List<ApprovalDecision> findAll() {
        return approvalRepository.findAll();
    }
}
