package com.okcapital.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
class GatewayControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void routeMetadataIsVisible() throws Exception {
        webTestClient.get()
                .uri("/api/gateway/routes")
                .exchange()
                .expectStatus().isOk();
    }
}
