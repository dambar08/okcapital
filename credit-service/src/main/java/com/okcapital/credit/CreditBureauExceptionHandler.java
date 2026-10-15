package com.okcapital.credit;

import com.okcapital.credit.service.CreditBureauException;
import com.okcapital.shared.error.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CreditBureauExceptionHandler {

    @ExceptionHandler(CreditBureauException.class)
    public ResponseEntity<ApiErrorResponse> handleCreditBureauException(
            CreditBureauException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(exception.getStatus())
                .body(ApiErrorResponse.of(
                        exception.getStatus().value(),
                        exception.getStatus().getReasonPhrase(),
                        exception.getMessage(),
                        request.getRequestURI(),
                        Map.of()
                ));
    }
}
