package com.okcapital.document.service;

import java.io.InputStream;

public interface DocumentStorage {
    void store(String storageKey, InputStream content, long size, String contentType);

    InputStream open(String storageKey);

    void delete(String storageKey);
}
