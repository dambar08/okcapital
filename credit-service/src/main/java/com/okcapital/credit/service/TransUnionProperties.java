package com.okcapital.credit.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.credit-bureau.transunion")
public record TransUnionProperties(String endpoint, String accessToken) {
}
