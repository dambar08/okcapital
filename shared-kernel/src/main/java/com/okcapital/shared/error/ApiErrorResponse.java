package com.okcapital.shared.error;

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
            int status,
            String error,
            String message,
            String path,
            Map<String, String> details
    ) {
        return new ApiErrorResponse(Instant.now(), status, error, message, path, details);
    }
}
