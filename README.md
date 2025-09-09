# Project Repository

This repository contains the Receipt and Expense Management Platform.

Backend (Spring Boot) highlights:
- REST APIs for document upload, OCR extraction, auto-categorization, versioning, search, and admin monitoring.
- MongoDB integration using Spring Data with environment variables:
  - MONGODB_URL (e.g., mongodb://user:pass@host:27017)
  - MONGODB_DB (database name)
- OpenAPI docs available at /swagger-ui.html and /api-docs when the service is running.

Run:
1) Ensure environment variables MONGODB_URL and MONGODB_DB are set in the container runtime.
2) From receipt_processing_backend: ./gradlew bootRun

Example endpoints:
- POST /api/documents/upload (multipart/form-data: userId, title?, notes?, file)
- POST /api/documents/{id}/versions (multipart/form-data: notes?, file)
- GET  /api/documents/{id}
- POST /api/documents/search (JSON: query?, category?, userId?)
- POST /api/documents/{id}/categorize (JSON: { "category": "Meals" })
- GET  /api/admin/stats