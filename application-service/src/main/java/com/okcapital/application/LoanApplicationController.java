package com.okcapital.application;

import com.okcapital.application.dto.CreateApplicationRequest;
import com.okcapital.application.dto.ApplicationStateResponse;
import com.okcapital.application.dto.TransitionApplicationRequest;
import com.okcapital.application.entity.ApplicationStatus;
import com.okcapital.application.entity.InvalidApplicationTransitionException;
import com.okcapital.application.entity.LoanApplication;
import com.okcapital.application.service.LoanApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
public class LoanApplicationController {
    private final LoanApplicationService applicationService;

    public LoanApplicationController(LoanApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public List<LoanApplication> list() {
        return applicationService.findAll();
    }

    @GetMapping("/states")
    public List<ApplicationStateResponse> listStates() {
        return Arrays.stream(ApplicationStatus.values())
                .map(ApplicationStateResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<LoanApplication> create(@RequestBody CreateApplicationRequest request) {
        LoanApplication application = applicationService.createApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @PostMapping("/{applicationId}/submit")
    public LoanApplication submit(@PathVariable UUID applicationId) {
        return applicationService.submitApplication(applicationId);
    }

    @PostMapping("/{applicationId}/transition")
    public LoanApplication transition(
            @PathVariable UUID applicationId,
            @Valid @RequestBody TransitionApplicationRequest request) {
        return applicationService.transitionApplication(applicationId, request.status());
    }

    @ExceptionHandler(InvalidApplicationTransitionException.class)
    public ResponseEntity<String> handleInvalidTransition(InvalidApplicationTransitionException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
