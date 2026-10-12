package com.okcapital.document;

import com.okcapital.document.entity.Document;
import com.okcapital.document.service.DocumentService;
import com.okcapital.document.service.DocumentStorageException;
import com.okcapital.document.service.StoredDocumentContent;
import com.okcapital.document.dto.DocumentResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public List<DocumentResponse> list(
            @RequestParam(required = false) String applicationId,
            @RequestParam(required = false) String customerId) {
        return documentService.findAll(applicationId, customerId).stream()
                .map(DocumentResponse::from)
                .toList();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> upload(
            @RequestParam(value = "applicationId", required = false) String applicationId,
            @RequestParam(value = "customerId", required = false) String customerId,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) {
        Document document = documentService.store(applicationId, customerId, documentType, file);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{documentId}")
                        .buildAndExpand(document.getId())
                        .toUri())
                .body(DocumentResponse.from(document));
    }

    @GetMapping("/{documentId}")
    public DocumentResponse get(@PathVariable UUID documentId) {
        return DocumentResponse.from(documentService.findById(documentId));
    }

    @GetMapping("/{documentId}/content")
    public ResponseEntity<InputStreamResource> download(@PathVariable UUID documentId) {
        StoredDocumentContent content = documentService.openContent(documentId);
        Document document = content.document();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getContentType()))
                .contentLength(document.getSizeBytes())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(document.getFileName(), StandardCharsets.UTF_8)
                        .build()
                        .toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(new InputStreamResource(content.content()));
    }

    @ExceptionHandler(DocumentRequestException.class)
    public ResponseEntity<String> handleInvalidRequest(DocumentRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(DocumentNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(DocumentStorageException.class)
    public ResponseEntity<String> handleStorageFailure() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Document storage is currently unavailable");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleUploadTooLarge() {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("file exceeds the maximum allowed size");
    }
}
