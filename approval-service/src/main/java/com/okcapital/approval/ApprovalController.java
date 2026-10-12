package com.okcapital.approval;

import com.okcapital.approval.dto.CreateApprovalRequest;
import com.okcapital.approval.entity.ApprovalDecision;
import com.okcapital.approval.service.ApprovalService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {
    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping
    public List<ApprovalDecision> list() {
        return approvalService.findAll();
    }

    @PostMapping
    public ResponseEntity<ApprovalDecision> create(@RequestBody CreateApprovalRequest request) {
        ApprovalDecision decision = approvalService.approveApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(decision);
    }
}
