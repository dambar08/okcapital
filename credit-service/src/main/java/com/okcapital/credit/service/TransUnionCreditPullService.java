package com.okcapital.credit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.okcapital.credit.dto.CreditPullResponse;
import com.okcapital.credit.dto.TransUnionPullRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class TransUnionCreditPullService {
    private final RestClient restClient;
    private final TransUnionProperties properties;

    public TransUnionCreditPullService(RestClient.Builder restClientBuilder,
            TransUnionProperties properties) {
        this.restClient = restClientBuilder.build();
        this.properties = properties;
    }

    public CreditPullResponse pull(TransUnionPullRequest request) {
        if (properties.endpoint() == null || properties.endpoint().isBlank()
                || properties.accessToken() == null || properties.accessToken().isBlank()) {
            throw new CreditBureauException(HttpStatus.SERVICE_UNAVAILABLE,
                    "TransUnion endpoint and access token must be configured");
        }

        try {
            JsonNode report = restClient.post()
                    .uri(properties.endpoint())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.accessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(new TransUnionRequest(
                            request.firstName(),
                            request.lastName(),
                            request.dateOfBirth().toString(),
                            request.ssn(),
                            request.streetAddress(),
                            request.city(),
                            request.state(),
                            request.postalCode()))
                    .retrieve()
                    .body(JsonNode.class);

            if (report == null || report.isNull()) {
                throw new CreditBureauException(HttpStatus.BAD_GATEWAY,
                        "TransUnion returned an empty credit report");
            }
            return new CreditPullResponse(request.applicationId(), "TRANSUNION", report);
        } catch (RestClientException exception) {
            throw new CreditBureauException(HttpStatus.BAD_GATEWAY,
                    "TransUnion credit pull failed");
        }
    }

    private record TransUnionRequest(
            String firstName,
            String lastName,
            String dateOfBirth,
            String ssn,
            String streetAddress,
            String city,
            String state,
            String postalCode
    ) {
    }
}
