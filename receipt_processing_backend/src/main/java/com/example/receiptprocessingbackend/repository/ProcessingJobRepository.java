package com.example.receiptprocessingbackend.repository;

import com.example.receiptprocessingbackend.domain.processingJob;
import com.example.receiptprocessingbackend.domain.ProcessingJob;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Repository for CRUD operations and custom queries on ProcessingJob entities.
 */
@Repository
public interface ProcessingJobRepository extends MongoRepository<ProcessingJob, String> {

    // PUBLIC_INTERFACE
    /**
     * Find all jobs for a document.
     * @param documentId the related document id
     * @return list of jobs
     */
    List<ProcessingJob> findByDocumentId(String documentId);

    // PUBLIC_INTERFACE
    /**
     * Find all jobs for a specific document version.
     * @param documentVersionId the document version id
     * @return list of jobs
     */
    List<ProcessingJob> findByDocumentVersionId(String documentVersionId);

    // PUBLIC_INTERFACE
    /**
     * Find jobs by type (e.g., OCR, EXTRACTION, CATEGORIZATION).
     * @param type job type
     * @return list of jobs
     */
    List<ProcessingJob> findByType(String type);

    // PUBLIC_INTERFACE
    /**
     * Find jobs by status (e.g., QUEUED, RUNNING, COMPLETED, FAILED).
     * @param status job status
     * @return list of jobs
     */
    List<ProcessingJob> findByStatus(String status);
}
