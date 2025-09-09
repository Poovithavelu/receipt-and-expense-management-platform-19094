package com.example.receiptprocessingbackend.repository;

import com.example.receiptprocessingbackend.model.Document;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Documents.
 */
@Repository
public interface DocumentRepository extends MongoRepository<Document, String> {
    List<Document> findByUserId(String userId);
    List<Document> findByCategory(String category);
    List<Document> findByTitleContainingIgnoreCase(String title);
    List<Document> findByOcrTextContainingIgnoreCase(String query);
}
