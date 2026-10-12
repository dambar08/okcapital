package com.okcapital.customer;

import com.okcapital.customer.dto.CreateCustomerRequest;
import com.okcapital.customer.entity.Customer;
import com.okcapital.customer.repository.CustomerRepository;
import com.okcapital.customer.service.CustomerService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    @Test
    void createCustomerReturnsPersistedEntity() {
        CustomerRepository repository = mock(CustomerRepository.class);
        when(repository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CustomerService service = new CustomerService(repository);

        Customer customer = service.createCustomer(new CreateCustomerRequest(
                "Jane",
                "Doe",
                "jane.doe@example.com",
                "123456789",
                "123 Main St",
                "Boston",
                "02110",
                "US"));

        assertNotNull(customer.getCustomerId());
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
    }
}
