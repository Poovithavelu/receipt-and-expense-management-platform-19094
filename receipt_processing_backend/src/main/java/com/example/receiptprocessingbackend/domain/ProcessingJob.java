package com.example.receiptprocessingbackend.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

/**
 * PUBLIC_INTERFACE
 * Represents an asynchronous processing job (OCR, field extraction, categorization).
 * Tracks status and results for a given document/version.
 */
@Document(collection = "processing_jobs")
@CompoundIndex(name = "doc_ver_idx", def = "{'document_id': 1, 'document_version_id': 1}")
public class ProcessingJob {

    @Id
    private String id;

    /**
     * The type of job, e.g., OCR, EXTRACTION, CATEGORIZATION, FULL_PIPELINE.
     */
    @Indexed
    private String type;

    /**
     * The current status of the job, e.g., QUEUED, RUNNING, COMPLETED, FAILED, CANCELLED.
     */
    @Indexed
    private String status;

    /**
     * Reference to the document id this job relates to.
     */
    @Indexed
    @Field("document_id")
    private String documentId;

    /**
     * Optional reference to a specific document version id.
     */
    @Indexed
    @Field("document_version_id")
    private String documentVersionId;

    /**
     * Free-form parameters for the job (e.g., OCR language, model options).
     */
    private Map<String, Object> params;

    /**
     * Results summary or pointers to where results are stored.
     */
    private Map<String, Object> results;

    /**
     * Error information if any failure occurs.
     */
    @Field("error_message")
    private String errorMessage;

    @CreatedDate
    @Indexed
    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // PUBLIC_INTERFACE
    public ProcessingJob() {}

    // PUBLIC_INTERFACE
    public ProcessingJob(String type, String status, String documentId, String documentVersionId) {
        this.type = type;
        this.status = status;
        this.documentId = documentId;
        this.documentVersionId = documentVersionId;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    // Getters and setters (PUBLIC_INTERFACE)
    /** PUBLIC_INTERFACE */
    public String getId() { return id; }
    /** PUBLIC_INTERFACE */
    public void setId(String id) { this.id = id; }

    /** PUBLIC_INTERFACE */
    public String getType() { return type; }
    /** PUBLIC_INTERFACE */
    public void setType(String type) { this.type = type; }

    /** PUBLIC_INTERFACE */
    public String getStatus() { return status; }
    /** PUBLIC_INTERFACE */
    public void setStatus(String status) { this.status = status; }

    /** PUBLIC_INTERFACE */
    public String getDocumentId() { return documentId; }
    /** PUBLIC_INTERFACE */
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    /** PUBLIC_INTERFACE */
    public String getDocumentVersionId() { return documentVersionId; }
    /** PUBLIC_INTERFACE */
    public void setDocumentVersionId(String documentVersionId) { this.documentVersionId = documentVersionId; }

    /** PUBLIC_INTERFACE */
    public Map<String, Object> getParams() { return params; }
    /** PUBLIC_INTERFACE */
    public void setParams(Map<String, Object> params) { this.params = params; }

    /** PUBLIC_INTERFACE */
    public Map<String, Object> getResults() { return results; }
    /** PUBLIC_INTERFACE */
    public void setResults(Map<String, Object> results) { this.results = results; }

    /** PUBLIC_INTERFACE */
    public String getErrorMessage() { return errorMessage; }
    /** PUBLIC_INTERFACE */
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    /** PUBLIC_INTERFACE */
    public Instant getCreatedAt() { return createdAt; }
    /** PUBLIC_INTERFACE */
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    /** PUBLIC_INTERFACE */
    public Instant getUpdatedAt() { return updatedAt; }
    /** PUBLIC_INTERFACE */
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
