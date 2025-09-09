package com.example.receiptprocessingbackend.model;

import java.time.Instant;
import java.util.List;

/**
 * Represents raw OCR text lines and confidence scores.
 */
public class OcrResult {
    private Instant processedAt;
    private String engine; // e.g., "tesseract"
    private List<TextLine> lines;
    private String fullText;

    public Instant getProcessedAt() { return processedAt; }
    public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }

    public String getEngine() { return engine; }
    public void setEngine(String engine) { this.engine = engine; }

    public List<TextLine> getLines() { return lines; }
    public void setLines(List<TextLine> lines) { this.lines = lines; }

    public String getFullText() { return fullText; }
    public void setFullText(String fullText) { this.fullText = fullText; }

    public static class TextLine {
        private String text;
        private Double confidence;

        public TextLine() {}
        public TextLine(String text, Double confidence) {
            this.text = text;
            this.confidence = confidence;
        }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }
    }
}
