package com.example.receiptprocessingbackend.model;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a specific version of an uploaded document including OCR and key fields.
 */
public class DocumentVersion {
    private String versionId;
    private int versionNumber;
    private Instant uploadedAt;
    private String originalFileName;
    private String contentType;
    private long sizeBytes;
    private String storagePath; // could be a GridFS id or external storage reference
    private OcrResult ocr;
    private Map<String, Object> keyFields; // e.g., total, date, vendor, tax, etc.

    public String getVersionId() { return versionId; }
    public void setVersionId(String versionId) { this.versionId = versionId; }

    public int getVersionNumber() { return versionNumber; }
    public void setVersionNumber(int versionNumber) { this.versionNumber = versionNumber; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; }

    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }

    public OcrResult getOcr() { return ocr; }
    public void setOcr(OcrResult ocr) { this.ocr = ocr; }

    public Map<String, Object> getKeyFields() { return keyFields; }
    public void setKeyFields(Map<String, Object> keyFields) { this.keyFields = keyFields; }
}
