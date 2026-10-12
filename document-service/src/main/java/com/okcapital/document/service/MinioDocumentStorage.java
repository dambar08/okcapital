package com.okcapital.document.service;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

@Component
public class MinioDocumentStorage implements DocumentStorage {
    private final MinioClient minioClient;
    private final String bucket;

    public MinioDocumentStorage(MinioClient minioClient, MinioStorageProperties properties) {
        this.minioClient = minioClient;
        this.bucket = properties.bucket();
    }

    @Override
    public void store(String storageKey, InputStream content, long size, String contentType) {
        try {
            ensureBucket();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(storageKey)
                    .stream(content, size, -1)
                    .contentType(contentType)
                    .build());
        } catch (MinioException | IOException | GeneralSecurityException exception) {
            throw new DocumentStorageException("Unable to store document", exception);
        }
    }

    @Override
    public InputStream open(String storageKey) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(storageKey)
                    .build());
        } catch (MinioException | IOException | GeneralSecurityException exception) {
            throw new DocumentStorageException("Unable to retrieve document", exception);
        }
    }

    @Override
    public void delete(String storageKey) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(storageKey)
                    .build());
        } catch (MinioException | IOException | GeneralSecurityException exception) {
            throw new DocumentStorageException("Unable to remove document", exception);
        }
    }

    private void ensureBucket() throws MinioException, IOException, GeneralSecurityException {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }
}
