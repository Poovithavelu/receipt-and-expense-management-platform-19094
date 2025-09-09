package com.example.receiptprocessingbackend.repository;

import com.example.receiptprocessingbackend.domain.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Repository for CRUD operations and custom queries on Document entities.
 */
@Repository
public interface DocumentRepository extends MongoRepository<Document, String> {

    // PUBLIC_INTERFACE
    /**
     * Find documents by owner id.
     * @param ownerId the owner id to filter by
     * @return list of documents belonging to the owner
     */
    List<Document> findByOwnerId(String ownerId);

    // PUBLIC_INTERFACE
    /**
     * Find documents by status.
     * @param status the status to filter by (e.g., NEW, PROCESSING, READY)
     * @return list of matching documents
     */
    List<Document> findByStatus(String status);

    // PUBLIC_INTERFACE
    /**
     * Find documents by type.
     * @param type document type/category (e.g., RECEIPT, INVOICE)
     * @return list of matching documents
     */
    List<Document> findByType(String type);

    // PUBLIC_INTERFACE
    /**
     * Find documents with a specific tag.
     * @param tags the tag to match
     * @return list of matching documents
     */
    List<Document> findByTagsContaining(String tags);
}
