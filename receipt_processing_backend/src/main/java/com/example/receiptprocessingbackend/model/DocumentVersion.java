package com.example.receiptprocessingbackend.model;

import java.time.Instant;

/**
 * Embedded document version info.
 */
public class DocumentVersion {

    private int versionNumber;
    private Instant createdAt = Instant.now();
    private String notes;
    private String storageRef; // blob/file storage ref for this version
    private String ocrText; // OCR text for this version
    private String category; // optional category for this version

    public DocumentVersion() {}

    public DocumentVersion(int versionNumber, String notes, String storageRef, String ocrText, String category) {
        this.versionNumber = versionNumber;
        this.notes = notes;
        this.storageRef = storageRef;
        this.ocrText = ocrText;
        this.category = category;
        this.createdAt = Instant.now();
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getNotes() {
        return notes;
    }

    public String getStorageRef() {
        return storageRef;
    }

    public String getOcrText() {
        return ocrText;
    }

    public String getCategory() {
        return category;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStorageRef(String storageRef) {
        this.storageRef = storageRef;
    }

    public void setOcrText(String ocrText) {
        this.ocrText = ocrText;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
