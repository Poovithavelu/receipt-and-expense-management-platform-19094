# Project Repository

This is the initial README file for the project.

## receipt_processing_backend

- Spring Boot 3 API for:
  - Document upload (multipart/form-data)
  - OCR trigger and key-field extraction (mock implementation in this scaffold)
  - Expense categorization (heuristic)
  - Version management (upload new versions, view history)
  - Search/filtering by user, category, vendor, and free text

### Run
- Configure environment variables (see `receipt_processing_backend/.env.example`)
- Start the app:
  - `./gradlew bootRun` (env variables should be loaded by your runtime)

### MongoDB
The backend reads `spring.data.mongodb.uri` and `spring.data.mongodb.database` from environment variables:
- `MONGODB_URI`
- `MONGODB_DATABASE`

Please refer to `document_database/connection_config.md` in the document_database container for connection details and production guidance.

### API Docs
- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/api-docs`