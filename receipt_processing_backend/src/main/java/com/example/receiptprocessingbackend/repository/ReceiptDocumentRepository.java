package com.example.receiptprocessingbackend.repository;

import com.example.receiptprocessingbackend.model.ReceiptDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface ReceiptDocumentRepository extends MongoRepository<ReceiptDocument, String> {
    Page<ReceiptDocument> findByUserId(String userId, Pageable pageable);

    Page<ReceiptDocument> findByUserIdAndCategoryContainingIgnoreCaseAndVendorContainingIgnoreCase(
            String userId, String category, String vendor, Pageable pageable
    );

    List<ReceiptDocument> findByTransactionDateBetween(Instant start, Instant end);
}
