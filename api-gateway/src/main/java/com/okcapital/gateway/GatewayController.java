package com.okcapital.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    @GetMapping("/routes")
    public ResponseEntity<Map<String, String>> routes() {
        Map<String, String> routes = Map.of(
                "customers", "http://localhost:8082/api/customers",
                "applications", "http://localhost:8083/api/applications",
                "products", "http://localhost:8084/api/products",
                "auth", "http://localhost:8081/api/auth");

        return ResponseEntity.ok(routes);
    }
}
