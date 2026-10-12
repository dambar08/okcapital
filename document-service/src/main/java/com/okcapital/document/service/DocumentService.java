package com.okcapital.document.service;

import com.okcapital.document.DocumentNotFoundException;
import com.okcapital.document.DocumentRequestException;
import com.okcapital.document.entity.Document;
import com.okcapital.document.repository.DocumentRepository;
import com.okcapital.shared.UuidV8Generator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    private static final String DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_OCTET_STREAM_VALUE;

    private final DocumentRepository documentRepository;
    private final DocumentStorage documentStorage;
    private final long maximumFileSizeBytes;

    public DocumentService(
            DocumentRepository documentRepository,
            DocumentStorage documentStorage,
            @Value("${app.documents.max-file-size:10MB}") DataSize maximumFileSize) {
        this.documentRepository = documentRepository;
        this.documentStorage = documentStorage;
        this.maximumFileSizeBytes = maximumFileSize.toBytes();
    }

    public Document store(
            String applicationId,
            String customerId,
            String documentType,
            MultipartFile file) {
        String normalizedApplicationId = normalizeOptional(applicationId);
        String normalizedCustomerId = normalizeOptional(customerId);
        if (normalizedApplicationId == null && normalizedCustomerId == null) {
            throw new DocumentRequestException("applicationId or customerId is required");
        }
        if (normalizedApplicationId != null && normalizedApplicationId.length() > 100) {
            throw new DocumentRequestException("applicationId must be at most 100 characters");
        }
        if (normalizedCustomerId != null && normalizedCustomerId.length() > 100) {
            throw new DocumentRequestException("customerId must be at most 100 characters");
        }

        String normalizedDocumentType = normalizeRequired(documentType, "documentType");
        if (normalizedDocumentType.length() > 100) {
            throw new DocumentRequestException("documentType must be at most 100 characters");
        }
        if (file == null || file.isEmpty()) {
            throw new DocumentRequestException("file must not be empty");
        }
        if (file.getSize() > maximumFileSizeBytes) {
            throw new DocumentRequestException("file exceeds the maximum allowed size");
        }

        String fileName = normalizeRequired(file.getOriginalFilename(), "file name");
        if (fileName.length() > 255) {
            throw new DocumentRequestException("file name must be at most 255 characters");
        }
        String contentType = normalizeContentType(file.getContentType());
        if (contentType.length() > 255) {
            throw new DocumentRequestException("file content type must be at most 255 characters");
        }

        UUID documentId = UuidV8Generator.generate();
        String storageKey = documentId.toString();
        try (var input = file.getInputStream()) {
            documentStorage.store(storageKey, input, file.getSize(), contentType);
        } catch (IOException exception) {
            throw new DocumentStorageException("Unable to read uploaded document", exception);
        }

        Document document = new Document(
                documentId,
                normalizedApplicationId,
                normalizedCustomerId,
                normalizedDocumentType,
                fileName,
                contentType,
                file.getSize(),
                storageKey);
        try {
            return documentRepository.save(document);
        } catch (RuntimeException exception) {
            try {
                documentStorage.delete(storageKey);
            } catch (DocumentStorageException cleanupException) {
                exception.addSuppressed(cleanupException);
            }
            throw exception;
        }
    }

    public List<Document> findAll(String applicationId, String customerId) {
        String normalizedApplicationId = normalizeOptional(applicationId);
        String normalizedCustomerId = normalizeOptional(customerId);
        if (normalizedApplicationId != null && normalizedCustomerId != null) {
            return documentRepository.findByApplicationIdAndCustomerIdOrderByCreatedAtDesc(
                    normalizedApplicationId, normalizedCustomerId);
        }
        if (normalizedApplicationId != null) {
            return documentRepository.findByApplicationIdOrderByCreatedAtDesc(normalizedApplicationId);
        }
        if (normalizedCustomerId != null) {
            return documentRepository.findByCustomerIdOrderByCreatedAtDesc(normalizedCustomerId);
        }
        return documentRepository.findAllByOrderByCreatedAtDesc();
    }

    public Document findById(UUID documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));
    }

    public StoredDocumentContent openContent(UUID documentId) {
        Document document = findById(documentId);
        return new StoredDocumentContent(document, documentStorage.open(document.getStorageKey()));
    }

    private String normalizeContentType(String contentType) {
        if (!StringUtils.hasText(contentType)) {
            return DEFAULT_CONTENT_TYPE;
        }
        try {
            return MediaType.parseMediaType(contentType).toString();
        } catch (org.springframework.http.InvalidMediaTypeException exception) {
            throw new DocumentRequestException("file content type is invalid");
        }
    }

    private String normalizeRequired(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new DocumentRequestException(fieldName + " is required");
        }
        return value.trim();
    }

    private String normalizeOptional(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
