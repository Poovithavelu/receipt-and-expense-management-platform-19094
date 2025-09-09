package com.example.receiptprocessingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Search request DTO.
 */
public class SearchRequest {

    @Schema(description = "Free text query to search in title and OCR text", example = "coffee")
    private String query;

    @Schema(description = "Filter by category", example = "Meals")
    private String category;

    @Schema(description = "Filter by user", example = "user-123")
    private String userId;

    public SearchRequest() {}

    public SearchRequest(String query, String category, String userId) {
        this.query = query;
        this.category = category;
        this.userId = userId;
    }

    public String getQuery() {
        return query;
    }

    public String getCategory() {
        return category;
    }

    public String getUserId() {
        return userId;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
