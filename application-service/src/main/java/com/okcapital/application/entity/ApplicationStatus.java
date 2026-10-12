package com.okcapital.application.entity;

import java.util.EnumSet;
import java.util.Set;

public enum ApplicationStatus {
    DRAFT("Resume Application", "User started entering data but hasn't finalized submission."),
    SUBMITTED("Application Received", "Received by system; awaiting automated checks or manual triage."),
    IN_PROGRESS("Processing", "Back-office processors are verifying income, identity, and fraud risk."),
    INFO_REQUESTED("Action Required", "Paused while waiting for the applicant to upload missing documentation."),
    UNDERWRITING("Under Review", "Risk assessment evaluation by an automated scoring engine or manual underwriter."),
    APPROVED_CONDITIONAL("Conditionally Approved",
            "Approved pending milestones such as a property appraisal or collateral check."),
    APPROVED_FINAL("Approved", "Fully validated; underwriting has authorized the loan structure."),
    READY_FOR_SIGNING("Awaiting Signature", "Contracts generated and awaiting the applicant's electronic signature."),
    FUNDING_IN_PROGRESS("Funding In Progress", "Contracts executed; the system is executing the fund transfer."),
    FUNDED("Funded", "Funds successfully transferred; loan transitioned to servicing."),
    FUNDING_FAILED("Funding Failed", "Fund transfer failed."),
    DECLINED("Declined", "Application declined."),
    WITHDRAWN("Withdrawn", "Application withdrawn."),
    ABANDONED("Abandoned", "Draft application abandoned."),
    EXPIRED("Expired", "Application expired while awaiting action.");

    private final String displayLabel;
    private final String description;

    ApplicationStatus(String displayLabel, String description) {
        this.displayLabel = displayLabel;
        this.description = description;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public String getDescription() {
        return description;
    }

    public Set<ApplicationStatus> getAllowedTransitions() {
        return Set.copyOf(switch (this) {
            case DRAFT -> EnumSet.of(SUBMITTED, ABANDONED);
            case SUBMITTED -> EnumSet.of(IN_PROGRESS, WITHDRAWN);
            case IN_PROGRESS -> EnumSet.of(INFO_REQUESTED, UNDERWRITING, WITHDRAWN, DECLINED);
            case INFO_REQUESTED -> EnumSet.of(IN_PROGRESS, WITHDRAWN, EXPIRED);
            case UNDERWRITING -> EnumSet.of(APPROVED_CONDITIONAL, APPROVED_FINAL, DECLINED, WITHDRAWN);
            case APPROVED_CONDITIONAL -> EnumSet.of(APPROVED_FINAL, DECLINED, WITHDRAWN, EXPIRED);
            case APPROVED_FINAL -> EnumSet.of(READY_FOR_SIGNING, WITHDRAWN);
            case READY_FOR_SIGNING -> EnumSet.of(FUNDING_IN_PROGRESS, WITHDRAWN, EXPIRED);
            case FUNDING_IN_PROGRESS -> EnumSet.of(FUNDED, FUNDING_FAILED);
            case FUNDED, FUNDING_FAILED, DECLINED, WITHDRAWN, ABANDONED, EXPIRED ->
                EnumSet.noneOf(ApplicationStatus.class);
        });
    }

    public boolean canTransitionTo(ApplicationStatus nextStatus) {
        return nextStatus != null && getAllowedTransitions().contains(nextStatus);
    }
}
