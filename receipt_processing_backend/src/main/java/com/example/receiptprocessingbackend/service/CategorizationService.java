package com.example.receiptprocessingbackend.service;

import org.springframework.stereotype.Service;

/**
 * Service that categorizes documents based on simple heuristics of OCR text/title.
 */
@Service
public class CategorizationService {

    // PUBLIC_INTERFACE
    public String autoCategorize(String title, String ocrText) {
        String text = (title == null ? "" : title + " ") + (ocrText == null ? "" : ocrText);
        String lower = text.toLowerCase();

        if (lower.contains("uber") || lower.contains("lyft") || lower.contains("taxi")) return "Transportation";
        if (lower.contains("hotel") || lower.contains("airbnb")) return "Lodging";
        if (lower.contains("flight") || lower.contains("airlines")) return "Travel";
        if (lower.contains("coffee") || lower.contains("restaurant") || lower.contains("meal")) return "Meals";
        if (lower.contains("office") || lower.contains("stationery")) return "Office Supplies";
        if (lower.contains("invoice") || lower.contains("receipt")) return "General";

        return "Uncategorized";
    }
}
