package com.okcapital.application.service;

import com.okcapital.shared.UuidV8Generator;
import com.okcapital.application.dto.CreateApplicationRequest;
import com.okcapital.application.entity.ApplicationStatus;
import com.okcapital.application.entity.LoanApplication;
import com.okcapital.application.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class LoanApplicationService {
    private final ApplicationRepository applicationRepository;

    public LoanApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public LoanApplication createApplication(CreateApplicationRequest request) {
        LoanApplication application = new LoanApplication(
                UuidV8Generator.generate(),
                request.customerId(),
                request.productId(),
                request.requestedAmount(),
                request.termMonths(),
                ApplicationStatus.DRAFT);
        return applicationRepository.save(application);
    }

    public LoanApplication submitApplication(UUID applicationId) {
        return transitionApplication(applicationId, ApplicationStatus.SUBMITTED);
    }

    public LoanApplication transitionApplication(UUID applicationId, ApplicationStatus nextStatus) {
        LoanApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("Application not found: " + applicationId));
        application.transitionTo(nextStatus);
        return applicationRepository.save(application);
    }

    public List<LoanApplication> findAll() {
        return applicationRepository.findAll();
    }
}
