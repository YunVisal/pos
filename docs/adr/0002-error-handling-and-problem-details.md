# ADR 0002 — Error handling with Problem Details in `platform-web`

- **Status:** Accepted
- **Date:** 2026-10-07 (Day 3 morning)

## Business context

The Sokha Mart owner-portal and POS app will call 14 services. Today, a blank unit code
or a duplicate code ends up as a generic **500** with Spring's default body, so the client
can't tell "you typed something wrong" from "the server crashed". If every service
handles errors its own way, the frontends need 14 kinds of error parsing.

## Decision

### 1. Error handling lives in a shared library, `platform-web`

```
platform/platform-web   GlobalExceptionHandler, BusinessException hierarchy
services/*              depend on platform-web
```

Not in `service-template`: the template is **copied** for each new service, and copies
drift. A dependency gives every service the same version.

### 2. Status codes

| Situation | Example | Status |
| --- | --- | --- |
| Request is malformed or invalid on its own | blank code, wrong JSON type | **400** |
| Resource doesn't exist | unit `PCS` not found | **404** |
| Valid request, conflicts with current state | unit `KG` already exists | **409** |
| Valid request, a business rule says no | insufficient stock to sell 5 Coca-Cola | **422** |
| Our bug | unexpected exception | **500**, no internals leaked |

### 3. Business exception hierarchy

```
RuntimeException
 └─ BusinessException (abstract)          platform-web
     ├─ NotFoundException       → 404
     ├─ ConflictException       → 409
     └─ BusinessRuleException   → 422
          ▲
 DuplicateUnitCodeException extends ConflictException   (service domain)
```

- The handler catches only the platform types and never imports a feature's exception.
- Exceptions carry **no `HttpStatus`**: the domain doesn't know HTTP. The handler maps
  type → status.
- 400s are **not** business exceptions. They come from Bean Validation / Jackson
  (`MethodArgumentNotValidException`, `HttpMessageNotReadableException`) and are mapped
  by the handler.

### 4. Validate in both the DTO and the domain

| | DTO (`@NotBlank`, `@Size`, ...) | Domain constructor |
| --- | --- | --- |
| Job | Friendly feedback, every bad field at once | Invariant: an invalid object can never exist |
| Covers | API only | API, CSV import, events, ... |

If a domain `IllegalArgumentException` reaches the handler from the API, a DTO rule is missing.

### 5. One error body: RFC 9457 Problem Details

`Content-Type: application/problem+json`

```json
{
  "type": "https://errors.sokhamart.com/conflict",
  "title": "Conflict",
  "status": 409,
  "detail": "Unit code already exists: KG",
  "instance": "/api/v1/units"
}
```

Validation errors add an `errors` extension with one entry per field. The request ID
extension comes on Day 5 (`platform-logging`).

## Alternatives considered

- **Handler in each service / in the template:** rejected, because copies drift.
- **Exception carries `HttpStatus`:** simpler, but couples the domain to HTTP.
- **Custom error JSON (`errorCode`, `message`):** works, but it's a home-made standard. RFC 9457
  is supported natively by Spring (`ProblemDetail`), clients and gateways.
- **One `BadInputException` business type for 400:** rejected; input-shape errors are
  caught by validation before business code runs.

## Consequences

- Service domains import `com.sokhamart.platform.web` exception types, so the domain depends
  on a platform library. Accepted for now; revisit if `platform-web` pulls too much into
  the domain (option: move the exceptions to a plain-Java `platform-core`).
- Every new error type has to fit one of the categories, or we add a category to the platform.
