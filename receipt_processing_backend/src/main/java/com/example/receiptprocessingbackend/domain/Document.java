package com.example.receiptprocessingbackend.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PUBLIC_INTERFACE
 * Represents a logical document uploaded by a user (e.g., receipt, invoice).
 * Stores high-level metadata and references to versions (content snapshots over time).
 */
@Document(collection = "documents")
public class Document {

    @Id
    private String id;

    /**
     * Human-friendly title/name for the document.
     */
    @Indexed
    private String title;

    /**
     * Type/category of the document (e.g., RECEIPT, INVOICE, STATEMENT).
     */
    @Indexed
    private String type;

    /**
     * Owner or user identifier. Can be an email, UUID, or auth subject.
     */
    @Indexed
    @Field("owner_id")
    private String ownerId;

    /**
     * Tags used for search and categorization.
     */
    private List<String> tags = new ArrayList<>();

    /**
     * Reference ids to DocumentVersion entities.
     * Keeping as list of ids decouples size of the doc document from large embedded versions.
     */
    @Field("version_ids")
    private List<String> versionIds = new ArrayList<>();

    /**
     * Id of the latest (current) version for quick access.
     */
    @Indexed
    @Field("current_version_id")
    private String currentVersionId;

    /**
     * Optional status (e.g., NEW, PROCESSING, READY, ERROR) to reflect processing state.
     */
    @Indexed
    private String status;

    @CreatedDate
    @Indexed
    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // PUBLIC_INTERFACE
    public Document() {}

    // PUBLIC_INTERFACE
    public Document(String title, String type, String ownerId) {
        this.title = title;
        this.type = type;
        this.ownerId = ownerId;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.status = "NEW";
    }

    // Getters and setters (PUBLIC_INTERFACE)
    /** PUBLIC_INTERFACE */
    public String getId() { return id; }
    /** PUBLIC_INTERFACE */
    public void setId(String id) { this.id = id; }

    /** PUBLIC_INTERFACE */
    public String getTitle() { return title; }
    /** PUBLIC_INTERFACE */
    public void setTitle(String title) { this.title = title; }

    /** PUBLIC_INTERFACE */
    public String getType() { return type; }
    /** PUBLIC_INTERFACE */
    public void setType(String type) { this.type = type; }

    /** PUBLIC_INTERFACE */
    public String getOwnerId() { return ownerId; }
    /** PUBLIC_INTERFACE */
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    /** PUBLIC_INTERFACE */
    public List<String> getTags() { return tags; }
    /** PUBLIC_INTERFACE */
    public void setTags(List<String> tags) { this.tags = tags; }

    /** PUBLIC_INTERFACE */
    public List<String> getVersionIds() { return versionIds; }
    /** PUBLIC_INTERFACE */
    public void setVersionIds(List<String> versionIds) { this.versionIds = versionIds; }

    /** PUBLIC_INTERFACE */
    public String getCurrentVersionId() { return currentVersionId; }
    /** PUBLIC_INTERFACE */
    public void setCurrentVersionId(String currentVersionId) { this.currentVersionId = currentVersionId; }

    /** PUBLIC_INTERFACE */
    public String getStatus() { return status; }
    /** PUBLIC_INTERFACE */
    public void setStatus(String status) { this.status = status; }

    /** PUBLIC_INTERFACE */
    public Instant getCreatedAt() { return createdAt; }
    /** PUBLIC_INTERFACE */
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    /** PUBLIC_INTERFACE */
    public Instant getUpdatedAt() { return updatedAt; }
    /** PUBLIC_INTERFACE */
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // PUBLIC_INTERFACE
    public void addVersionId(String versionId) {
        Objects.requireNonNull(versionId, "versionId cannot be null");
        this.versionIds.add(versionId);
        this.currentVersionId = versionId;
        this.updatedAt = Instant.now();
    }
}
