package com.example.receiptprocessingbackend.controller;

import com.example.receiptprocessingbackend.model.ReceiptDocument;
import com.example.receiptprocessingbackend.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints for triggering OCR and categorization.
 */
@RestController
@RequestMapping("/api/processing")
@Tag(name = "Processing")
public class ProcessingController {

    private final ReceiptService service;

    public ProcessingController(ReceiptService service) {
        this.service = service;
    }

    // PUBLIC_INTERFACE
    @PostMapping("/{id}/ocr")
    @Operation(summary = "Trigger OCR", description = "Re-runs OCR and key field extraction on the latest version of the document.")
    public ResponseEntity<ReceiptDocument> triggerOcr(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(service.triggerOcr(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
