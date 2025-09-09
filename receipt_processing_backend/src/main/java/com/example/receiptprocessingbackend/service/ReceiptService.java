package com.example.receiptprocessingbackend.service;

import com.example.receiptprocessingbackend.dto.SearchRequest;
import com.example.receiptprocessingbackend.model.DocumentVersion;
import com.example.receiptprocessingbackend.model.OcrResult;
import com.example.receiptprocessingbackend.model.ReceiptDocument;
import com.example.receiptprocessingbackend.repository.ReceiptDocumentRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Business logic for handling receipt documents, OCR, categorization, and search.
 */
@Service
public class ReceiptService {

    private final ReceiptDocumentRepository repository;

    public ReceiptService(ReceiptDocumentRepository repository) {
        this.repository = repository;
    }

    // PUBLIC_INTERFACE
    public ReceiptDocument uploadNewDocument(String userId, MultipartFile file, Map<String, Object> metadata) throws IOException {
        Assert.hasText(userId, "userId required");
        Assert.notNull(file, "file required");

        ReceiptDocument doc = new ReceiptDocument();
        doc.setUserId(userId);
        doc.setTitle(FilenameUtils.getBaseName(file.getOriginalFilename()));
        doc.setStatus("UPLOADED");
        doc.setCreatedAt(Instant.now());
        doc.setUpdatedAt(Instant.now());
        doc.setMetadata(metadata != null ? metadata : new HashMap<>());

        DocumentVersion version = buildVersionFromFile(file, 1);
        doc.getVersions().add(version);

        // naive extraction and categorization
        applyOcrAndExtraction(doc, version);

        return repository.save(doc);
    }

    // PUBLIC_INTERFACE
    public ReceiptDocument uploadNewVersion(String documentId, MultipartFile file) throws IOException {
        ReceiptDocument doc = repository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + documentId));
        int nextVersionNum = doc.getVersions().stream()
                .map(DocumentVersion::getVersionNumber)
                .max(Integer::compareTo).orElse(0) + 1;

        DocumentVersion version = buildVersionFromFile(file, nextVersionNum);
        doc.getVersions().add(version);
        doc.setUpdatedAt(Instant.now());
        doc.setStatus("UPDATED");

        applyOcrAndExtraction(doc, version);

        return repository.save(doc);
    }

    // PUBLIC_INTERFACE
    public Optional<ReceiptDocument> getDocument(String id) {
        return repository.findById(id);
    }

    // PUBLIC_INTERFACE
    public List<DocumentVersion> getHistory(String documentId) {
        ReceiptDocument doc = repository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + documentId));
        return doc.getVersions().stream()
                .sorted(Comparator.comparingInt(DocumentVersion::getVersionNumber))
                .collect(Collectors.toList());
    }

    // PUBLIC_INTERFACE
    public Page<ReceiptDocument> search(SearchRequest req) {
        int page = Math.max(req.getPage(), 0);
        int size = Math.min(Math.max(req.getSize(), 1), 100);
        PageRequest pageable = PageRequest.of(page, size);

        String userId = req.getUserId();
        if (StringUtils.isBlank(userId)) {
            throw new IllegalArgumentException("userId is required for search");
        }

        // Basic indexed filters
        String category = Optional.ofNullable(req.getCategory()).orElse("");
        String vendor = Optional.ofNullable(req.getVendor()).orElse("");

        Page<ReceiptDocument> base = repository
                .findByUserIdAndCategoryContainingIgnoreCaseAndVendorContainingIgnoreCase(userId, category, vendor, pageable);

        // Optional in-memory free-text filtering on title/vendor/last OCR text
        if (StringUtils.isNotBlank(req.getQuery())) {
            String q = req.getQuery().toLowerCase(Locale.ROOT);
            List<ReceiptDocument> filtered = base.getContent().stream()
                    .filter(d -> containsFreeText(d, q))
                    .collect(Collectors.toList());
            return new org.springframework.data.domain.PageImpl<>(filtered, pageable, filtered.size());
        }
        return base;
    }

    // PUBLIC_INTERFACE
    public ReceiptDocument triggerOcr(String documentId) {
        ReceiptDocument doc = repository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + documentId));

        // Re-run OCR on latest version
        DocumentVersion latest = doc.getVersions().stream()
                .max(Comparator.comparingInt(DocumentVersion::getVersionNumber))
                .orElseThrow(() -> new IllegalStateException("Document has no versions"));

        latest.setOcr(mockOcr(latest.getOriginalFileName()));
        latest.setKeyFields(extractKeyFields(latest.getOcr()));
        updateDerivedFieldsFromKeyFields(doc, latest);

        doc.setUpdatedAt(Instant.now());
        doc.setStatus("PROCESSED");
        return repository.save(doc);
    }

    private DocumentVersion buildVersionFromFile(MultipartFile file, int versionNumber) throws IOException {
        DocumentVersion version = new DocumentVersion();
        version.setVersionId(UUID.randomUUID().toString());
        version.setVersionNumber(versionNumber);
        version.setUploadedAt(Instant.now());
        version.setOriginalFileName(file.getOriginalFilename());
        version.setContentType(file.getContentType());
        version.setSizeBytes(file.getSize());
        // For this scaffold, we are not persisting binary content; storagePath can be set to placeholder
        version.setStoragePath("memory://" + version.getVersionId());

        // Mock OCR immediately for demo purposes
        OcrResult ocr = mockOcr(file.getOriginalFilename());
        version.setOcr(ocr);
        version.setKeyFields(extractKeyFields(ocr));
        return version;
    }

    private void applyOcrAndExtraction(ReceiptDocument doc, DocumentVersion version) {
        updateDerivedFieldsFromKeyFields(doc, version);
        doc.setStatus("PROCESSED");
    }

    private void updateDerivedFieldsFromKeyFields(ReceiptDocument doc, DocumentVersion version) {
        Map<String, Object> keyFields = version.getKeyFields();
        if (keyFields == null) return;
        doc.setVendor(Objects.toString(keyFields.getOrDefault("vendor", doc.getVendor()), null));
        doc.setCategory(Objects.toString(keyFields.getOrDefault("category", doc.getCategory()), null));
        Object amt = keyFields.get("amount");
        if (amt instanceof Number) doc.setAmount(((Number) amt).doubleValue());
        doc.setCurrency(Objects.toString(keyFields.getOrDefault("currency", doc.getCurrency()), null));
        Object ts = keyFields.get("transactionDate");
        if (ts instanceof Instant) doc.setTransactionDate((Instant) ts);
        doc.setUpdatedAt(Instant.now());
    }

    private boolean containsFreeText(ReceiptDocument d, String q) {
        if (StringUtils.containsIgnoreCase(StringUtils.defaultString(d.getTitle()), q)) return true;
        if (StringUtils.containsIgnoreCase(StringUtils.defaultString(d.getVendor()), q)) return true;
        // search in latest OCR full text
        Optional<OcrResult> latestOcr = d.getVersions().stream()
                .max(Comparator.comparingInt(DocumentVersion::getVersionNumber))
                .map(DocumentVersion::getOcr);
        return latestOcr.map(ocr -> StringUtils.containsIgnoreCase(
                StringUtils.defaultString(ocr.getFullText()), q)).orElse(false);
        }

    private OcrResult mockOcr(String fileName) {
        OcrResult ocr = new OcrResult();
        ocr.setProcessedAt(Instant.now());
        ocr.setEngine("mock-ocr");
        String base = StringUtils.defaultString(fileName, "receipt");
        ocr.setFullText("Mock OCR text for " + base + "\nVendor: Coffee Shop\nTotal: 12.50 USD\nDate: 2024-12-01");
        ocr.setLines(Arrays.asList(
                new OcrResult.TextLine("Mock OCR text for " + base, 0.99),
                new OcrResult.TextLine("Vendor: Coffee Shop", 0.97),
                new OcrResult.TextLine("Total: 12.50 USD", 0.96),
                new OcrResult.TextLine("Date: 2024-12-01", 0.94)
        ));
        return ocr;
    }

    private Map<String, Object> extractKeyFields(OcrResult ocr) {
        Map<String, Object> fields = new HashMap<>();
        if (ocr == null || ocr.getFullText() == null) return fields;
        String text = ocr.getFullText();

        // Heuristic parsing
        String vendor = parseValueAfter(text, "Vendor:");
        if (StringUtils.isNotBlank(vendor)) fields.put("vendor", vendor.trim());

        String total = parseValueAfter(text, "Total:");
        if (StringUtils.isNotBlank(total)) {
            // parse amount and currency
            String[] parts = total.trim().split("\\s+");
            try {
                double amount = Double.parseDouble(parts[0].replaceAll("[^0-9.]", ""));
                fields.put("amount", amount);
                if (parts.length > 1) fields.put("currency", parts[1]);
            } catch (NumberFormatException ignored) {}
        }

        String dateStr = parseValueAfter(text, "Date:");
        if (StringUtils.isNotBlank(dateStr)) {
            try {
                Instant instant = Instant.parse(dateStr.trim() + "T00:00:00Z");
                fields.put("transactionDate", instant);
            } catch (Exception ignored) {}
        }

        // Naive category rule
        String lower = text.toLowerCase(Locale.ROOT);
        if (lower.contains("coffee") || lower.contains("cafe")) {
            fields.put("category", "Meals");
        } else if (lower.contains("uber") || lower.contains("lyft") || lower.contains("taxi")) {
            fields.put("category", "Transport");
        } else {
            fields.put("category", "Uncategorized");
        }
        return fields;
    }

    private String parseValueAfter(String text, String label) {
        int idx = text.indexOf(label);
        if (idx < 0) return null;
        int start = idx + label.length();
        int end = text.indexOf('\n', start);
        if (end < 0) end = text.length();
        return text.substring(start, end).trim();
    }
}
