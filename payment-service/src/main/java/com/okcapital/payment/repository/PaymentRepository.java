package com.okcapital.payment.repository;

import com.okcapital.payment.entity.PaymentRecord;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentRecord, java.util.UUID> {
}
