package com.okcapital.document.entity;

import com.okcapital.shared.IdentifiableEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "documents")
@AttributeOverride(name = "id", column = @Column(name = "document_id"))
public class Document extends IdentifiableEntity {
    @Column(length = 100)
    private String applicationId;

    @Column(length = 100)
    private String customerId;

    @Column(nullable = false, length = 100)
    private String documentType;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 255)
    private String contentType;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(nullable = false, unique = true, length = 36)
    private String storageKey;

    protected Document() {
    }

    public Document(
            UUID id,
            String applicationId,
            String customerId,
            String documentType,
            String fileName,
            String contentType,
            long sizeBytes,
            String storageKey) {
        super(id);
        this.applicationId = applicationId;
        this.customerId = customerId;
        this.documentType = documentType;
        this.fileName = fileName;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.storageKey = storageKey;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public String getStorageKey() {
        return storageKey;
    }
}
