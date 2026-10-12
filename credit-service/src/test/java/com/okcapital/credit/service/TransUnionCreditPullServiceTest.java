package com.okcapital.credit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okcapital.credit.dto.CreditPullResponse;
import com.okcapital.credit.dto.TransUnionPullRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

class TransUnionCreditPullServiceTest {
    private static final String ENDPOINT = "https://tu.example.test/credit-pulls";
    private static final String ACCESS_TOKEN = "test-access-token";

    private MockRestServiceServer server;
    private TransUnionCreditPullService service;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        service = new TransUnionCreditPullService(builder,
                new TransUnionProperties(ENDPOINT, ACCESS_TOKEN));
    }

    @Test
    void sendsApplicantDataAndReturnsProviderReport() throws Exception {
        UUID applicationId = UUID.fromString("4ed64a0d-88d8-48d3-b93d-275801acd3f2");
        TransUnionPullRequest request = request(applicationId);
        String response = """
                {"creditScore":720,"providerReference":"tu-123"}
                """;

        server.expect(requestTo(ENDPOINT))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "firstName":"Ada",
                          "lastName":"Lovelace",
                          "dateOfBirth":"1815-12-10",
                          "ssn":"123-45-6789",
                          "streetAddress":"12 St James's Square",
                          "city":"London",
                          "state":"NY",
                          "postalCode":"10001"
                        }
                        """, JsonCompareMode.STRICT))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        CreditPullResponse result = service.pull(request);

        JsonNode expectedReport = new ObjectMapper().readTree(response);
        assertEquals(applicationId, result.applicationId());
        assertEquals("TRANSUNION", result.provider());
        assertEquals(expectedReport, result.report());
        server.verify();
    }

    @Test
    void reportsProviderErrorsWithoutExposingProviderResponseBody() {
        server.expect(requestTo(ENDPOINT))
                .andRespond(withStatus(UNAUTHORIZED).body("sensitive provider error"));

        CreditBureauException exception = assertThrows(CreditBureauException.class,
                () -> service.pull(request(UUID.randomUUID())));

        assertEquals(502, exception.getStatus().value());
        assertEquals("TransUnion credit pull failed", exception.getMessage());
        server.verify();
    }

    @Test
    void failsExplicitlyWhenTransUnionIsNotConfigured() {
        TransUnionCreditPullService unconfiguredService = new TransUnionCreditPullService(
                RestClient.builder(), new TransUnionProperties("", ""));

        CreditBureauException exception = assertThrows(CreditBureauException.class,
                () -> unconfiguredService.pull(request(UUID.randomUUID())));

        assertEquals(503, exception.getStatus().value());
        assertEquals("TransUnion endpoint and access token must be configured", exception.getMessage());
    }

    private TransUnionPullRequest request(UUID applicationId) {
        return new TransUnionPullRequest(applicationId, "Ada", "Lovelace",
                LocalDate.of(1815, 12, 10), "123-45-6789", "12 St James's Square",
                "London", "NY", "10001");
    }
}
