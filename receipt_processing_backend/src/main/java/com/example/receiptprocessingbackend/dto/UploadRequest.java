package com.example.receiptprocessingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request DTO for document upload.
 */
public class UploadRequest {

    @Schema(description = "User ID who uploads", example = "user-123")
    @NotBlank
    private String userId;

    @Schema(description = "Optional title for the document", example = "March Invoice")
    private String title;

    @Schema(description = "Optional notes", example = "Business lunch")
    private String notes;

    @Schema(description = "The uploaded file (image/PDF)")
    @NotNull
    private MultipartFile file;

    public UploadRequest() {}

    public UploadRequest(String userId, String title, String notes, MultipartFile file) {
        this.userId = userId;
        this.title = title;
        this.notes = notes;
        this.file = file;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
