package com.okcapital.shared.error;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {
    private final ApiExceptionHandler handler = new ApiExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    void autoConfigurationRegistersHandlerForServletApplications() {
        new WebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ApiErrorHandlingAutoConfiguration.class))
                .run(context -> assertThat(context).hasSingleBean(ApiExceptionHandler.class));
    }

    @Test
    void mapsInvalidRequestsToBadRequestWithConsistentBody() {
        request.setRequestURI("/api/customers");

        var response = handler.handleIllegalArgument(
                new IllegalArgumentException("email is required"),
                request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .extracting(ApiErrorResponse::status, ApiErrorResponse::message, ApiErrorResponse::path)
                .containsExactly(400, "email is required", "/api/customers");
    }

    @Test
    void includesFieldValidationErrorsInResponseDetails() {
        BindException exception = new BindException(new Object(), "request");
        exception.addError(new FieldError("request", "email", "must be a valid email address"));

        var response = handler.handleValidationException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().details())
                .containsEntry("email", "must be a valid email address");
    }

    @Test
    void preservesResponseStatusExceptionStatusAndReason() {
        ResponseStatusException exception = new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Notification not found"
        );

        var response = handler.handleResponseStatus(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().message()).isEqualTo("Notification not found");
    }

    @Test
    void hidesUnexpectedExceptionDetailsFromClients() {
        var response = handler.handleUnexpectedException(
                new IllegalStateException("database credentials leaked"),
                request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().message()).isEqualTo("An unexpected error occurred");
        assertThat(response.getBody().message()).doesNotContain("database credentials");
    }
}
