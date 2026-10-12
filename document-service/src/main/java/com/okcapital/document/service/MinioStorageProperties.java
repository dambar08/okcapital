package com.okcapital.document.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.documents.minio")
public record MinioStorageProperties(String bucket) {
}
