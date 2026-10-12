package com.okcapital.customer.service;

import com.okcapital.shared.UuidV8Generator;
import com.okcapital.customer.dto.CreateCustomerRequest;
import com.okcapital.customer.entity.Customer;
import com.okcapital.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer(
                UuidV8Generator.generate(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phoneNumber(),
                request.streetAddress(),
                request.city(),
                request.postalCode(),
                request.country());
        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
