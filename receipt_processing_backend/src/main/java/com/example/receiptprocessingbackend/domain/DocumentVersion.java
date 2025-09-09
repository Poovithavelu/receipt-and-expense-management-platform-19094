package com.example.receiptprocessingbackend.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * PUBLIC_INTERFACE
 * Represents a version of a Document with stored content location and processing outputs.
 * Intended to be immutable once created, except for processing-related enrichment fields.
 */
@Document(collection = "document_versions")
public class DocumentVersion {

    @Id
    private String id;

    /**
     * Reference to parent Document id.
     */
    @Indexed
    @Field("document_id")
    private String documentId;

    /**
     * Version number increasing per document.
     */
    @Indexed
    @Field("version_number")
    private Integer versionNumber;

    /**
     * Original file name as uploaded.
     */
    @Field("file_name")
    private String fileName;

    /**
     * MIME type of the stored file.
     */
    @Field("mime_type")
    private String mimeType;

    /**
     * Byte size of the stored file.
     */
    @Field("size_bytes")
    private Long sizeBytes;

    /**
     * Storage location reference (e.g., S3 path, GridFS id).
     */
    @Field("storage_ref")
    private String storageRef;

    /**
     * OCR raw text result, if available.
     */
    @Field("ocr_text")
    private String ocrText;

    /**
     * Key fields extraction result (e.g., totals, dates, vendor).
     */
    @Field("extracted_fields")
    private Map<String, Object> extractedFields;

    /**
     * Detected categories for expense classification.
     */
    @Field("categories")
    private List<String> categories;

    /**
     * Processing state for this version (e.g., PENDING, PROCESSING, COMPLETED, ERROR).
     */
    @Indexed
    private String status;

    /**
     * Error message if processing failed.
     */
    @Field("error_message")
    private String errorMessage;

    @CreatedDate
    @Indexed
    @Field("created_at")
    private Instant createdAt;

    // PUBLIC_INTERFACE
    public DocumentVersion() {}

    // PUBLIC_INTERFACE
    public DocumentVersion(String documentId, Integer versionNumber, String fileName, String mimeType, Long sizeBytes, String storageRef) {
        this.documentId = documentId;
        this.versionNumber = versionNumber;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
        this.storageRef = storageRef;
        this.status = "PENDING";
        this.createdAt = Instant.now();
    }

    // Getters and Setters (PUBLIC_INTERFACE)
    /** PUBLIC_INTERFACE */
    public String getId() { return id; }
    /** PUBLIC_INTERFACE */
    public void setId(String id) { this.id = id; }

    /** PUBLIC_INTERFACE */
    public String getDocumentId() { return documentId; }
    /** PUBLIC_INTERFACE */
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    /** PUBLIC_INTERFACE */
    public Integer getVersionNumber() { return versionNumber; }
    /** PUBLIC_INTERFACE */
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }

    /** PUBLIC_INTERFACE */
    public String getFileName() { return fileName; }
    /** PUBLIC_INTERFACE */
    public void setFileName(String fileName) { this.fileName = fileName; }

    /** PUBLIC_INTERFACE */
    public String getMimeType() { return mimeType; }
    /** PUBLIC_INTERFACE */
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    /** PUBLIC_INTERFACE */
    public Long getSizeBytes() { return sizeBytes; }
    /** PUBLIC_INTERFACE */
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }

    /** PUBLIC_INTERFACE */
    public String getStorageRef() { return storageRef; }
    /** PUBLIC_INTERFACE */
    public void setStorageRef(String storageRef) { this.storageRef = storageRef; }

    /** PUBLIC_INTERFACE */
    public String getOcrText() { return ocrText; }
    /** PUBLIC_INTERFACE */
    public void setOcrText(String ocrText) { this.ocrText = ocrText; }

    /** PUBLIC_INTERFACE */
    public Map<String, Object> getExtractedFields() { return extractedFields; }
    /** PUBLIC_INTERFACE */
    public void setExtractedFields(Map<String, Object> extractedFields) { this.extractedFields = extractedFields; }

    /** PUBLIC_INTERFACE */
    public List<String> getCategories() { return categories; }
    /** PUBLIC_INTERFACE */
    public void setCategories(List<String> categories) { this.categories = categories; }

    /** PUBLIC_INTERFACE */
    public String getStatus() { return status; }
    /** PUBLIC_INTERFACE */
    public void setStatus(String status) { this.status = status; }

    /** PUBLIC_INTERFACE */
    public String getErrorMessage() { return errorMessage; }
    /** PUBLIC_INTERFACE */
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    /** PUBLIC_INTERFACE */
    public Instant getCreatedAt() { return createdAt; }
    /** PUBLIC_INTERFACE */
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
