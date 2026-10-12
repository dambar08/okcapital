package com.okcapital.application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Entity;
import jakarta.persistence.Convert;
import jakarta.persistence.Table;

import com.okcapital.shared.IdentifiableEntity;

import java.util.UUID;

@Entity
@Table(name = "loan_applications")
@AttributeOverride(name = "id", column = @Column(name = "application_id"))
public class LoanApplication extends IdentifiableEntity {
    @Column(nullable = false)
    private String customerId;
    @Column(nullable = false)
    private String productId;
    @Column(nullable = false)
    private double requestedAmount;
    @Column(nullable = false)
    private int termMonths;
    @Convert(converter = ApplicationStatusConverter.class)
    @Column(nullable = false)
    private ApplicationStatus status;

    protected LoanApplication() {
    }

    public LoanApplication(UUID applicationId, String customerId, String productId,
            double requestedAmount, int termMonths, ApplicationStatus status) {
        super(applicationId);
        this.customerId = customerId;
        this.productId = productId;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
        this.status = status;
    }

    public UUID getApplicationId() {
        return getId();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getProductId() {
        return productId;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void transitionTo(ApplicationStatus nextStatus) {
        if (nextStatus == null) {
            throw new IllegalArgumentException("nextStatus must not be null");
        }
        if (!status.canTransitionTo(nextStatus)) {
            throw new InvalidApplicationTransitionException(status, nextStatus);
        }
        this.status = nextStatus;
    }
}
