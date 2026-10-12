package com.okcapital.document.service;

import com.okcapital.document.entity.Document;

import java.io.InputStream;

public record StoredDocumentContent(Document document, InputStream content) {
}
