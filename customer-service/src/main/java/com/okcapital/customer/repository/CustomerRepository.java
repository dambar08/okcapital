package com.okcapital.customer.repository;

import com.okcapital.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, java.util.UUID> {
}
