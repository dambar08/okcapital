package com.okcapital.document.service;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableConfigurationProperties(MinioStorageProperties.class)
public class MinioStorageConfiguration {
    @Bean
    public MinioClient minioClient(
            @Value("${app.documents.minio.endpoint}") String endpoint,
            @Value("${app.documents.minio.access-key}") String accessKey,
            @Value("${app.documents.minio.secret-key}") String secretKey) {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
