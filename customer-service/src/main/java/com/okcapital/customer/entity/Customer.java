package com.okcapital.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.okcapital.shared.IdentifiableEntity;

import java.util.UUID;

@Entity
@Table(name = "customers")
@AttributeOverride(name = "id", column = @Column(name = "customer_id"))
public class Customer extends IdentifiableEntity {
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    private String phoneNumber;
    private String streetAddress;
    private String city;
    private String postalCode;
    private String country;

    protected Customer() {
    }

    public Customer(UUID customerId, String firstName, String lastName, String email,
            String phoneNumber, String streetAddress, String city, String postalCode, String country) {
        super(customerId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public UUID getCustomerId() {
        return getId();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }
}
