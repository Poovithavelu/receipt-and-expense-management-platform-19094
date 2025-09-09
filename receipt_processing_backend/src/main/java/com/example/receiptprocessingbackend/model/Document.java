package com.example.receiptprocessingbackend.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user uploaded document along with metadata.
 */
@Document(collection = "documents")
public class Document {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String title;

    private String fileName;
    private String contentType;
    private long fileSize;

    private Instant uploadedAt = Instant.now();

    @Indexed
    private String category;

    private String notes;

    // Latest extracted OCR text (for quick access)
    private String ocrText;

    // Reference to stored binary file id/path (out of scope; placeholder)
    private String storageRef;

    // Versioning info
    private List<DocumentVersion> versions = new ArrayList<>();

    public Document() {}

    public Document(String userId, String title, String fileName, String contentType, long fileSize) {
        this.userId = userId;
        this.title = title;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadedAt = Instant.now();
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public String getCategory() {
        return category;
    }

    public String getNotes() {
        return notes;
    }

    public String getOcrText() {
        return ocrText;
    }

    public String getStorageRef() {
        return storageRef;
    }

    public List<DocumentVersion> getVersions() {
        return versions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setOcrText(String ocrText) {
        this.ocrText = ocrText;
    }

    public void setStorageRef(String storageRef) {
        this.storageRef = storageRef;
    }

    public void setVersions(List<DocumentVersion> versions) {
        this.versions = versions;
    }
}
