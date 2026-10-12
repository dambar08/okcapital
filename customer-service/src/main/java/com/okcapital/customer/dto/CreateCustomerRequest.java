package com.okcapital.customer.dto;

public record CreateCustomerRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String streetAddress,
        String city,
        String postalCode,
        String country
) {
    public CreateCustomerRequest {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
    }
}
