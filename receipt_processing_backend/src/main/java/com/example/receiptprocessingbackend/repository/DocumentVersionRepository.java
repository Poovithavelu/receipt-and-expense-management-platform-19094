package com.example.receiptprocessingbackend.repository;

import com.example.receiptprocessingbackend.domain.DocumentVersion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PUBLIC_INTERFACE
 * Repository for CRUD operations and custom queries on DocumentVersion entities.
 */
@Repository
public interface DocumentVersionRepository extends MongoRepository<DocumentVersion, String> {

    // PUBLIC_INTERFACE
    /**
     * Find all versions of a given document.
     * @param documentId the document id
     * @return list of versions
     */
    List<DocumentVersion> findByDocumentId(String documentId);

    // PUBLIC_INTERFACE
    /**
     * Find a particular version number for a document.
     * @param documentId the document id
     * @param versionNumber the version number
     * @return optional version
     */
    Optional<DocumentVersion> findByDocumentIdAndVersionNumber(String documentId, Integer versionNumber);

    // PUBLIC_INTERFACE
    /**
     * Find versions by status (e.g., PENDING, PROCESSING, COMPLETED).
     * @param status version status
     * @return list of versions
     */
    List<DocumentVersion> findByStatus(String status);
}
