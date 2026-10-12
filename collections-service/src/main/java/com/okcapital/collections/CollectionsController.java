package com.okcapital.collections;

import com.okcapital.collections.dto.CreateCollectionsCaseRequest;
import com.okcapital.collections.entity.CollectionsCase;
import com.okcapital.collections.service.CollectionsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/collections")
public class CollectionsController {

    private final CollectionsService service;

    public CollectionsController(CollectionsService service) {
        this.service = service;
    }

    @PostMapping("/cases")
    public ResponseEntity<CollectionsCase> createCase(@RequestBody CreateCollectionsCaseRequest request) {
        return ResponseEntity.ok(service.createCase(request));
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<CollectionsCase>> findByLoan(@PathVariable String loanId) {
        return ResponseEntity.ok(service.findByLoanId(loanId));
    }
}
