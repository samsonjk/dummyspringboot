# Dummy Spring Boot

Spring Boot sample service with seeded data and mixed API patterns intended for AI-driven testing agents.

## Highlights

- CRUD APIs for customers, products, orders, and support tickets
- action endpoints such as order confirmation, cancellation, ticket assignment, and stock adjustment
- search and filter endpoints for common QA scenarios
- dashboard summary endpoint for aggregate validation
- reset endpoint to restore a known in-memory dataset
- H2 database with JPA, validation, and integration tests

## Run

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

## Useful URLs

- `GET /api/health`
- `GET /api/dashboard/summary`
- `POST /api/test/reset`
- H2 console: `http://localhost:8080/h2-console`
