# ADR 0001 — Package structure and layering

- **Status:** Accepted
- **Date:** 2026-10-06 (Day 2 morning)

## Business context

Sokha Mart sells products in different units: piece, kg, box, case of 24. Every service
(Product, Inventory, Sales…) needs that list, so the Reference Data service owns it.

Today `service-template` has its controllers in one `api` package. If we keep adding
features that way, each of the 14 services ends up organised differently and nobody can
find anything. We need **one package structure that every service copies**.

## Decision

### 1. Four layers, with dependencies pointing inward

```
api  ──►  application  ──►  domain  ◄──  infrastructure
(HTTP)    (use case)        (model+rules)  (DB, implements domain's interface)
```

| Layer | Responsibility | Example for `Unit` |
| --- | --- | --- |
| `api` | HTTP: controllers, request/response DTOs, mapping | `UnitController`, `CreateUnitRequest`, `UnitResponse` |
| `application` | Orchestrates one use case: load, call the domain, save, return. No business rules. | `UnitService.create(...)` |
| `domain` | The model **and its rules**. An invalid object can't be built. Defines the repository interface it needs. | `Unit` (code 1–10 uppercase letters/digits, name not blank), `UnitRepository` |
| `infrastructure` | Talks to the outside world (DB, other services). Implements domain interfaces. | `InMemoryUnitRepository` (Day 2), JPA repository (Day 4) |

### 2. Dependency Inversion for persistence

`application` must not import `infrastructure`. Instead:

- `domain` defines `interface UnitRepository`
- `infrastructure` implements it
- `application` asks for the **interface** in its constructor; Spring injects the implementation

When Day 4 replaces in-memory storage with Postgres, **only `infrastructure` changes**.
Domain, application and api stay the same. (SOLID: the **D**, Dependency Inversion.)

### 3. Feature first, then layer

```
com.sokhamart.<service>.<feature>.{api, application, domain, infrastructure}

template/
  unit/
    api/            UnitController
    application/    UnitService
    domain/         Unit, UnitRepository
    infrastructure/ InMemoryUnitRepository
  currency/
    api/ application/ domain/ infrastructure/
```

## Alternatives considered

**Layer first** (`api/`, `application/`, `domain/`, `infrastructure/` at the top, all
features mixed inside). Rejected because:

- What changes together should sit together. A change to Category touches one folder
  with feature-first, but four large folders with layer-first.
- Real reuse (`BaseEntity`, error handling) belongs in the `platform-*` libraries, not in
  a shared `domain/` folder. Features reaching into each other's domain is coupling.
- Feature-first lets us make infrastructure classes package-private, hidden from other
  features.

## Consequences

- Every new service and feature follows this layout; reviews reject anything else.
- No repository calls in controllers (to be enforced by an ArchUnit rule, Day 2 evening).
- The existing `api/` package (Ping, Info) stays as is for now.
