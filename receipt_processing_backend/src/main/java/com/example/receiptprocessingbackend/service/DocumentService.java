package com.example.receiptprocessingbackend.service;

import com.example.receiptprocessingbackend.model.Document;
import com.example.receiptprocessingbackend.model.DocumentVersion;
import com.example.receiptprocessingbackend.repository.DocumentRepository;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service coordinating document persistence, OCR, and categorization.
 */
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final OcrService ocrService;
    private final CategorizationService categorizationService;

    public DocumentService(DocumentRepository documentRepository, OcrService ocrService, CategorizationService categorizationService) {
        this.documentRepository = documentRepository;
        this.ocrService = ocrService;
        this.categorizationService = categorizationService;
    }

    // PUBLIC_INTERFACE
    @Transactional
    public Document upload(String userId, String title, String notes, MultipartFile file) {
        String ocrText = ocrService.extractText(file);
        String category = categorizationService.autoCategorize(title, ocrText);

        Document doc = new Document(userId, title != null ? title : file.getOriginalFilename(),
                file.getOriginalFilename(), file.getContentType(), file.getSize());
        doc.setNotes(notes);
        doc.setOcrText(ocrText);
        doc.setCategory(category);
        doc.setStorageRef(generateStorageRef(file)); // placeholder

        DocumentVersion v1 = new DocumentVersion(1, notes, doc.getStorageRef(), ocrText, category);
        doc.getVersions().add(v1);

        return documentRepository.save(doc);
    }

    // PUBLIC_INTERFACE
    @Transactional
    public Document addVersion(String documentId, String notes, MultipartFile file) {
        Document doc = documentRepository.findById(documentId).orElseThrow(() -> new NoSuchElementException("Document not found"));
        int nextVersion = doc.getVersions().stream().map(DocumentVersion::getVersionNumber).max(Integer::compareTo).orElse(0) + 1;

        String ocrText = ocrService.extractText(file);
        String category = categorizationService.autoCategorize(doc.getTitle(), ocrText);
        String storageRef = generateStorageRef(file);

        DocumentVersion version = new DocumentVersion(nextVersion, notes, storageRef, ocrText, category);
        doc.getVersions().add(version);

        // Update latest pointers
        doc.setOcrText(ocrText);
        doc.setCategory(category);
        doc.setStorageRef(storageRef);
        doc.setUploadedAt(Instant.now());
        if (notes != null && !notes.isBlank()) {
            doc.setNotes(notes);
        }

        return documentRepository.save(doc);
    }

    // PUBLIC_INTERFACE
    public Optional<Document> getById(String id) {
        return documentRepository.findById(id);
    }

    // PUBLIC_INTERFACE
    public List<Document> search(String query, String category, String userId) {
        Set<String> ids = new LinkedHashSet<>();
        List<Document> results = new ArrayList<>();

        if (query != null && !query.isBlank()) {
            List<Document> inTitle = documentRepository.findByTitleContainingIgnoreCase(query);
            inTitle.forEach(d -> {
                if (ids.add(d.getId())) results.add(d);
            });
            List<Document> inText = documentRepository.findByOcrTextContainingIgnoreCase(query);
            inText.forEach(d -> {
                if (ids.add(d.getId())) results.add(d);
            });
        } else {
            results.addAll(documentRepository.findAll());
        }

        if (category != null && !category.isBlank()) {
            results = results.stream().filter(d -> category.equalsIgnoreCase(d.getCategory())).collect(Collectors.toList());
        }
        if (userId != null && !userId.isBlank()) {
            results = results.stream().filter(d -> userId.equals(d.getUserId())).collect(Collectors.toList());
        }
        return results;
    }

    // PUBLIC_INTERFACE
    @Transactional
    public Document setCategory(String id, String category) {
        Document doc = documentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Document not found"));
        doc.setCategory(category);

        // Also apply to latest version entry
        if (!doc.getVersions().isEmpty()) {
            DocumentVersion latest = doc.getVersions().get(doc.getVersions().size() - 1);
            latest.setCategory(category);
        }
        return documentRepository.save(doc);
    }

    // PUBLIC_INTERFACE
    public Map<String, Object> adminStats() {
        List<Document> all = documentRepository.findAll();
        Map<String, Long> byCategory = all.stream().collect(Collectors.groupingBy(d -> d.getCategory() == null ? "Uncategorized" : d.getCategory(), Collectors.counting()));
        Map<String, Long> byUser = all.stream().collect(Collectors.groupingBy(d -> d.getUserId() == null ? "unknown" : d.getUserId(), Collectors.counting()));
        long total = all.size();
        long totalSize = all.stream().mapToLong(Document::getFileSize).sum();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalDocuments", total);
        stats.put("totalStoredBytes", totalSize);
        stats.put("byCategory", byCategory);
        stats.put("byUser", byUser);
        return stats;
    }

    private String generateStorageRef(MultipartFile file) {
        // Placeholder for storage integration; only metadata is stored in Mongo for this demo.
        return "mem://" + UUID.randomUUID() + "/" + file.getOriginalFilename();
    }
}
