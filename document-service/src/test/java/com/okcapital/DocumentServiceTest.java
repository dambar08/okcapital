package com.okcapital.document;

import com.okcapital.document.entity.Document;
import com.okcapital.document.repository.DocumentRepository;
import com.okcapital.document.service.DocumentService;
import com.okcapital.document.service.DocumentStorage;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.unit.DataSize;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DocumentServiceTest {

    @Test
    void serviceBootstraps() {
        assertNotNull(new DocumentService(
                mock(DocumentRepository.class),
                mock(DocumentStorage.class),
                DataSize.ofMegabytes(10)));
    }

    @Test
    void storesFileAndPersistsItsMetadata() {
        DocumentRepository repository = mock(DocumentRepository.class);
        DocumentStorage storage = mock(DocumentStorage.class);
        when(repository.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));
        DocumentService service = new DocumentService(repository, storage, DataSize.ofMegabytes(10));
        MockMultipartFile file = new MockMultipartFile(
                "file", "identity.pdf", "application/pdf", "document".getBytes());

        Document document = service.store(" application-123 ", null, " identity ", file);

        assertNotNull(document.getId());
        assertEquals("application-123", document.getApplicationId());
        assertEquals("identity", document.getDocumentType());
        assertEquals("identity.pdf", document.getFileName());
        assertEquals("application/pdf", document.getContentType());
        assertEquals(file.getSize(), document.getSizeBytes());
        assertEquals(document.getId().toString(), document.getStorageKey());
        verify(storage).store(
                eq(document.getStorageKey()),
                any(InputStream.class),
                eq(file.getSize()),
                eq("application/pdf"));
        verify(repository).save(document);
    }

    @Test
    void removesStoredFileWhenMetadataCannotBeSaved() {
        DocumentRepository repository = mock(DocumentRepository.class);
        DocumentStorage storage = mock(DocumentStorage.class);
        when(repository.save(any(Document.class))).thenThrow(new IllegalStateException("database unavailable"));
        DocumentService service = new DocumentService(repository, storage, DataSize.ofMegabytes(10));
        MockMultipartFile file = new MockMultipartFile("file", "id.pdf", "application/pdf", new byte[] {1});

        assertThrows(
                IllegalStateException.class,
                () -> service.store("application-123", null, "identity", file));

        verify(storage).delete(any(String.class));
    }

    @Test
    void requiresAnApplicationOrCustomerAssociationBeforeWriting() {
        DocumentRepository repository = mock(DocumentRepository.class);
        DocumentStorage storage = mock(DocumentStorage.class);
        DocumentService service = new DocumentService(repository, storage, DataSize.ofMegabytes(10));
        MockMultipartFile file = new MockMultipartFile("file", "id.pdf", "application/pdf", new byte[] {1});

        assertThrows(DocumentRequestException.class, () -> service.store(null, " ", "identity", file));

        verifyNoInteractions(repository, storage);
    }

    @Test
    void rejectsFilesOverConfiguredSize() {
        DocumentRepository repository = mock(DocumentRepository.class);
        DocumentStorage storage = mock(DocumentStorage.class);
        DocumentService service = new DocumentService(repository, storage, DataSize.ofBytes(1));
        MockMultipartFile file = new MockMultipartFile("file", "id.pdf", "application/pdf", new byte[] {1, 2});

        assertThrows(
                DocumentRequestException.class,
                () -> service.store(null, "customer-123", "identity", file));

        verifyNoInteractions(repository, storage);
    }

    @Test
    void findsDocumentOrReportsNotFound() {
        DocumentRepository repository = mock(DocumentRepository.class);
        DocumentService service = new DocumentService(
                repository, mock(DocumentStorage.class), DataSize.ofMegabytes(10));
        UUID documentId = UUID.randomUUID();
        when(repository.findById(documentId)).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class, () -> service.findById(documentId));
    }
}
