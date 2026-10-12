package com.okcapital.auth.dto;

public record TokenResponse(String token, String tokenType, String expiresIn) {
}
