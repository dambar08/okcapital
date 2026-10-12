package com.okcapital.payment.service;

import com.okcapital.payment.dto.CreatePaymentRequest;
import com.okcapital.payment.entity.PaymentRecord;
import com.okcapital.payment.repository.PaymentRepository;

import com.okcapital.shared.UuidV8Generator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentRecord processPayment(CreatePaymentRequest request) {
        PaymentRecord payment = new PaymentRecord(
                UuidV8Generator.generate(),
                request.loanId(),
                request.accountId(),
                request.paymentMethod(),
                request.amount(),
                request.description(),
                "PROCESSED");
        return paymentRepository.save(payment);
    }

    public List<PaymentRecord> findAll() {
        return paymentRepository.findAll();
    }
}
