package com.example.receiptprocessingbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * OCR service that extracts text from images/PDFs.
 * In this implementation, a simple mock "OCR" is provided which returns the filename as text
 * for demonstration. Replace with real OCR (e.g., Tesseract or provider API) in production.
 */
@Service
public class OcrService {

    // PUBLIC_INTERFACE
    public String extractText(MultipartFile file) {
        // Mock implementation: return basic info as "extracted" text
        String base = "Extracted OCR for: " + file.getOriginalFilename();
        return base != null ? base : "No text";
    }
}
