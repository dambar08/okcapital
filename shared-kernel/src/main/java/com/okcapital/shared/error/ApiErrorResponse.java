package com.okcapital.shared.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> details
) {

    public ApiErrorResponse {
        details = Map.copyOf(details);
    }

    public static ApiErrorResponse of(
            HttpStatusCode status,
            String message,
            String path,
            Map<String, String> details
    ) {
        HttpStatus resolvedStatus = HttpStatus.resolve(status.value());
        String error = resolvedStatus == null ? "HTTP Error" : resolvedStatus.getReasonPhrase();
        return new ApiErrorResponse(Instant.now(), status.value(), error, message, path, details);
    }
}
