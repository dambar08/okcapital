package com.okcapital.credit;

import com.okcapital.credit.dto.CreditPullResponse;
import com.okcapital.credit.dto.TransUnionPullRequest;
import com.okcapital.credit.service.TransUnionCreditPullService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/credit-reports/transunion")
public class CreditPullController {
    private final TransUnionCreditPullService creditPullService;

    public CreditPullController(TransUnionCreditPullService creditPullService) {
        this.creditPullService = creditPullService;
    }

    @PostMapping("/pulls")
    public ResponseEntity<CreditPullResponse> pull(@Valid @RequestBody TransUnionPullRequest request) {
        return ResponseEntity.ok(creditPullService.pull(request));
    }
}
