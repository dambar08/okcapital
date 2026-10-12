package com.okcapital.payment;

import com.okcapital.payment.dto.CreatePaymentRequest;
import com.okcapital.payment.entity.PaymentRecord;
import com.okcapital.payment.service.PaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<PaymentRecord> list() {
        return paymentService.findAll();
    }

    @PostMapping
    public ResponseEntity<PaymentRecord> create(@RequestBody CreatePaymentRequest request) {
        PaymentRecord payment = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }
}
