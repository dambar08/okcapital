package com.okcapital.document;

import java.util.UUID;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(UUID documentId) {
        super("Document not found: " + documentId);
    }
}
