package com.example.receiptprocessingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Request to manually set or override a category.
 */
public class CategorizeRequest {

    @Schema(description = "New category to set", example = "Meals")
    @NotBlank
    private String category;

    public CategorizeRequest() {}

    public CategorizeRequest(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
