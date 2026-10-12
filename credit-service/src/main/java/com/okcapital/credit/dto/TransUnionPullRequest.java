package com.okcapital.credit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

public record TransUnionPullRequest(
        @NotNull UUID applicationId,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull @Past LocalDate dateOfBirth,
        @NotBlank @Pattern(regexp = "\\d{9}|\\d{3}-\\d{2}-\\d{4}") String ssn,
        @NotBlank String streetAddress,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String postalCode
) {
}
