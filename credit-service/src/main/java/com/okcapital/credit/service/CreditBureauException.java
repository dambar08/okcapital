package com.okcapital.credit.service;

import org.springframework.http.HttpStatus;

public class CreditBureauException extends RuntimeException {
    private final HttpStatus status;

    public CreditBureauException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
