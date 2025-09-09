package com.example.receiptprocessingbackend.dto;

/**
 * Response returned after a successful upload or new version creation.
 */
public class UploadResponse {
    private String documentId;
    private String versionId;
    private int versionNumber;
    private String status;

    public UploadResponse() {}

    public UploadResponse(String documentId, String versionId, int versionNumber, String status) {
        this.documentId = documentId;
        this.versionId = versionId;
        this.versionNumber = versionNumber;
        this.status = status;
    }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getVersionId() { return versionId; }
    public void setVersionId(String versionId) { this.versionId = versionId; }

    public int getVersionNumber() { return versionNumber; }
    public void setVersionNumber(int versionNumber) { this.versionNumber = versionNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
