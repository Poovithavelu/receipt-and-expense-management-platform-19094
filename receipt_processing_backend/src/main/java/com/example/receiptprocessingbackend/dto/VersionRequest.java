package com.example.receiptprocessingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request to add a new version of a document.
 */
public class VersionRequest {

    @Schema(description = "Optional notes for this version", example = "Edited brightness")
    private String notes;

    @Schema(description = "New file for the version")
    @NotNull
    private MultipartFile file;

    public VersionRequest() {}

    public VersionRequest(String notes, MultipartFile file) {
        this.notes = notes;
        this.file = file;
    }

    public String getNotes() {
        return notes;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
