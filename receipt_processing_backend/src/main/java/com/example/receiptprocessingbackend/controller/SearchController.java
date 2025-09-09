package com.example.receiptprocessingbackend.controller;

import com.example.receiptprocessingbackend.dto.SearchRequest;
import com.example.receiptprocessingbackend.model.ReceiptDocument;
import com.example.receiptprocessingbackend.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints for searching and filtering receipts.
 */
@RestController
@RequestMapping("/api/search")
@Tag(name = "Search")
public class SearchController {

    private final ReceiptService service;

    public SearchController(ReceiptService service) {
        this.service = service;
    }

    // PUBLIC_INTERFACE
    @GetMapping
    @Operation(
            summary = "Search receipts",
            description = "Searches for receipts by user, category, vendor, date range, and optional text query.\n"
                    + "Use query parameters: userId (required), query, category, vendor, startDate, endDate, page, size."
    )
    public ResponseEntity<Page<ReceiptDocument>> search(
            @RequestParam("userId") String userId,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "vendor", required = false) String vendor,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        SearchRequest req = new SearchRequest();
        req.setUserId(userId);
        req.setQuery(query);
        req.setCategory(category);
        req.setVendor(vendor);
        req.setPage(page);
        req.setSize(size);

        // Optional ISO-8601 Instant parsing if passed
        try {
            if (startDate != null && !startDate.isBlank()) {
                req.setStartDate(java.time.Instant.parse(startDate));
            }
            if (endDate != null && !endDate.isBlank()) {
                req.setEndDate(java.time.Instant.parse(endDate));
            }
        } catch (Exception ignored) {}

        Page<ReceiptDocument> results = service.search(req);
        return ResponseEntity.ok(results);
    }
}
