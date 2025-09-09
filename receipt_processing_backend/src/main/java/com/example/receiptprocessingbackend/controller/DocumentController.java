package com.example.receiptprocessingbackend.controller;

import com.example.receiptprocessingbackend.dto.UploadResponse;
import com.example.receiptprocessingbackend.model.DocumentVersion;
import com.example.receiptprocessingbackend.model.ReceiptDocument;
import com.example.receiptprocessingbackend.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Endpoints for uploading documents, creating versions, and retrieving history.
 */
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents")
@Validated
public class DocumentController {

    private final ReceiptService service;

    public DocumentController(ReceiptService service) {
        this.service = service;
    }

    // PUBLIC_INTERFACE
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a document",
            description = "Uploads a new receipt/invoice document and performs OCR and key field extraction.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Uploaded successfully",
                            content = @Content(schema = @Schema(implementation = UploadResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            }
    )
    public ResponseEntity<UploadResponse> upload(
            @RequestPart("userId") String userId,
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "metadata", required = false) MultiValueMap<String, String> metadataMap
    ) throws IOException {
        Map<String, Object> metadata = new HashMap<>();
        if (metadataMap != null) {
            metadataMap.forEach((k, v) -> metadata.put(k, v.size() == 1 ? v.getFirst(k) : v));
        }
        ReceiptDocument saved = service.uploadNewDocument(userId, file, metadata);
        DocumentVersion latest = saved.getVersions().stream()
                .max(Comparator.comparingInt(DocumentVersion::getVersionNumber))
                .orElse(null);
        UploadResponse resp = new UploadResponse(saved.getId(),
                latest != null ? latest.getVersionId() : null,
                latest != null ? latest.getVersionNumber() : 1,
                saved.getStatus());
        return ResponseEntity.ok(resp);
    }

    // PUBLIC_INTERFACE
    @PostMapping(value = "/{id}/versions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a new version for a document",
            description = "Creates a new version for an existing document and reprocesses OCR.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Version created",
                            content = @Content(schema = @Schema(implementation = UploadResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Document not found")
            }
    )
    public ResponseEntity<UploadResponse> uploadVersion(
            @PathVariable("id") String documentId,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        ReceiptDocument saved = service.uploadNewVersion(documentId, file);
        DocumentVersion latest = saved.getVersions().stream()
                .max(Comparator.comparingInt(DocumentVersion::getVersionNumber))
                .orElse(null);
        UploadResponse resp = new UploadResponse(saved.getId(),
                latest != null ? latest.getVersionId() : null,
                latest != null ? latest.getVersionNumber() : 1,
                saved.getStatus());
        return ResponseEntity.ok(resp);
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{id}")
    @Operation(summary = "Get document by id", description = "Retrieves a document with its metadata and versions.")
    public ResponseEntity<ReceiptDocument> getById(@PathVariable("id") String id) {
        return service.getDocument(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{id}/history")
    @Operation(summary = "Get version history", description = "Returns all versions for the specified document in ascending order.")
    public ResponseEntity<List<DocumentVersion>> history(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(service.getHistory(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
