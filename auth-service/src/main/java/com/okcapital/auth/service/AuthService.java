package com.okcapital.auth.service;

import com.okcapital.auth.dto.LoginRequest;
import com.okcapital.auth.dto.TokenResponse;

import com.okcapital.shared.UuidV8Generator;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    public TokenResponse login(LoginRequest request) {
        String email = request.email() == null ? "" : request.email().trim();
        String password = request.password() == null ? "" : request.password();

        if (email.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Email and password are required.");
        }

        String subject = email.toLowerCase();
        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(("okcapital-user:" + subject + ":" + UuidV8Generator.generate()).getBytes());

        return new TokenResponse(token, "Bearer", "3600");
    }
}
