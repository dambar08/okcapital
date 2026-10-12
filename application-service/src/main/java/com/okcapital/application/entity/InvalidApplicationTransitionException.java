package com.okcapital.application.entity;

public class InvalidApplicationTransitionException extends RuntimeException {

    public InvalidApplicationTransitionException(ApplicationStatus currentStatus, ApplicationStatus nextStatus) {
        super("Cannot transition application from " + currentStatus + " to " + nextStatus);
    }
}
