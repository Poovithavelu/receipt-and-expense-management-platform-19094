package com.example.receiptprocessingbackend.api;

import com.example.receiptprocessingbackend.dto.CategorizeRequest;
import com.example.receiptprocessingbackend.dto.SearchRequest;
import com.example.receiptprocessingbackend.dto.UploadRequest;
import com.example.receiptprocessingbackend.dto.VersionRequest;
import com.example.receiptprocessingbackend.model.Document;
import com.example.receiptprocessingbackend.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST endpoints for document operations.
 */
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "Endpoints for upload, OCR, categorization, versioning, and search")
@Validated
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // PUBLIC_INTERFACE
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Upload a document",
            description = "Uploads a document, runs OCR and auto-categorization, and creates version 1",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Upload successful",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            }
    )
    public ResponseEntity<Document> upload(
            @Parameter(description = "User ID", required = true) @RequestPart("userId") String userId,
            @Parameter(description = "Title", required = false) @RequestPart(value = "title", required = false) String title,
            @Parameter(description = "Notes", required = false) @RequestPart(value = "notes", required = false) String notes,
            @Parameter(description = "File", required = true) @RequestPart("file") MultipartFile file
    ) {
        Document created = documentService.upload(userId, title, notes, file);
        return ResponseEntity.ok(created);
    }

    // PUBLIC_INTERFACE
    @PostMapping(value = "/{id}/versions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Add a new version",
            description = "Adds a new version for a document, re-runs OCR and categorization",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Version added",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "404", description = "Document not found")
            }
    )
    public ResponseEntity<Document> addVersion(
            @PathVariable("id") String id,
            @Parameter(description = "Notes", required = false) @RequestPart(value = "notes", required = false) String notes,
            @Parameter(description = "File", required = true) @RequestPart("file") MultipartFile file
    ) {
        Document updated = documentService.addVersion(id, notes, file);
        return ResponseEntity.ok(updated);
    }

    // PUBLIC_INTERFACE
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get document by ID",
            description = "Returns the document with its versions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public ResponseEntity<Document> get(@PathVariable("id") String id) {
        Optional<Document> doc = documentService.getById(id);
        return doc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUBLIC_INTERFACE
    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Search documents",
            description = "Search by free text (title and OCR text), with optional filters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search results")
            }
    )
    public ResponseEntity<List<Document>> search(@Valid @RequestBody SearchRequest request) {
        List<Document> results = documentService.search(request.getQuery(), request.getCategory(), request.getUserId());
        return ResponseEntity.ok(results);
    }

    // PUBLIC_INTERFACE
    @PostMapping(value = "/{id}/categorize", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Set category",
            description = "Manually sets/overrides the category of a document",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "404", description = "Document not found")
            }
    )
    public ResponseEntity<Document> categorize(@PathVariable("id") String id, @Valid @RequestBody CategorizeRequest request) {
        Document updated = documentService.setCategory(id, request.getCategory());
        return ResponseEntity.ok(updated);
    }
}
