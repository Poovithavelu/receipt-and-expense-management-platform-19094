package com.example.receiptprocessingbackend.api;

import com.example.receiptprocessingbackend.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Admin endpoints for monitoring and statistics.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin monitoring and statistics")
public class AdminController {

    private final DocumentService documentService;

    public AdminController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // PUBLIC_INTERFACE
    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get admin stats", description = "Returns overall statistics and aggregates")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(documentService.adminStats());
    }
}
