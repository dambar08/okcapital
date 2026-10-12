package com.okcapital.payment;

import com.okcapital.payment.dto.CreatePaymentRequest;
import com.okcapital.payment.entity.PaymentRecord;
import com.okcapital.payment.repository.PaymentRepository;
import com.okcapital.payment.service.PaymentService;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @Test
    void processPaymentCreatesPaymentRecord() {
        PaymentRepository repository = mock(PaymentRepository.class);
        when(repository.save(any(PaymentRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));
        PaymentService service = new PaymentService(repository);

        PaymentRecord payment = service.processPayment(new CreatePaymentRequest(
                "loan-1001",
                "acct-2001",
                "CARD",
                new BigDecimal("1250.00"),
                "Monthly installment"));

        assertNotNull(payment.paymentId());
        assertEquals("loan-1001", payment.loanId());
        assertEquals("CARD", payment.paymentMethod());
        assertEquals(0, payment.amount().compareTo(new BigDecimal("1250.00")));
    }
}
