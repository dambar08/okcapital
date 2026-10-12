package com.okcapital.disbursement;

import com.okcapital.disbursement.dto.CreateDisbursementRequest;
import com.okcapital.disbursement.entity.DisbursementRecord;
import com.okcapital.disbursement.service.DisbursementService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/disbursements")
public class DisbursementController {

    private final DisbursementService service;

    public DisbursementController(DisbursementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DisbursementRecord> create(@RequestBody CreateDisbursementRequest request) {
        return ResponseEntity.ok(service.scheduleDisbursement(request));
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<DisbursementRecord>> findByLoan(@PathVariable String loanId) {
        return ResponseEntity.ok(service.findByLoanId(loanId));
    }
}
