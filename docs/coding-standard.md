# Sokha Mart coding standard

One page. Reviews reject code that breaks it. The reasoning lives in `docs/adr/`.

## 1. Packages and layers

`com.sokhamart.<service>.<feature>.{api, application, domain, infrastructure}`. Organize by feature first, then by layer (ADR 0001).

| Layer | Holds | May import |
| --- | --- | --- |
| `api` | Controllers, `Create…Request` / `…Response` records, mapping | `application`, `domain` (only to map) |
| `application` | One service per feature; orchestrates a use case; transactions (Day 4) | `domain` |
| `domain` | Model + rules, repository **interfaces**, domain exceptions | nothing from other layers, no Spring/JPA |
| `infrastructure` | Repository implementations, external clients | `domain` |

- Dependencies point inward: `api → application → domain ← infrastructure`.
- Controllers never call repositories. `ArchitectureTest` fails the build if they do.
- Features don't import each other's classes. Shared code goes into a `platform-*` library.

## 2. Models and DTOs

- The domain model protects itself: the constructor rejects invalid data, so an invalid `Unit` or `Currency` can't exist.
- Controllers never return domain objects. Map with `XResponse.from(x)`; for lists, use `stream().map(XResponse::from).toList()`.
- Request DTOs are named after the use case: `CreateCurrencyRequest`, not `CurrencyDto`.
- Money is always `BigDecimal`, never `double`.
- Tenant (`business_id`) comes from the token, never from the request body.

## 3. Naming and visibility

- Classes are nouns (`CurrencyService`); methods are verbs (`create`, `findAll`).
- Plural resource URLs under a version: `/api/v1/currencies`.
- Start package-private; use `public` only when another package needs it. Interfaces are public, implementations hidden.
- Use constructor injection only, with `final` fields. Never use `@Autowired` on fields in production code.
- No unused imports: run Organize Imports before every commit.

## 4. Errors

- Domain rules throw `IllegalArgumentException` or a named domain exception (`DuplicateCurrencyCodeException`).
- From Day 3: one error format, RFC 9457 Problem Details via `platform-web`.

## 5. Dependencies and builds

- Versions live only in `platform-parent` `<dependencyManagement>`; services declare dependencies without `<version>`.
- Test-only libraries get `<scope>test</scope>`.
- Schema changes go through Liquibase only, and an applied changeset is never edited (Day 4).

## 6. Tests

- Write the failing test first, at each layer: domain (plain JUnit), application (with a fake repository and no Spring annotations), api (MockMvc).
- Each test proves exactly one rule, and its input must reach that rule.
- Name tests `shouldXWhenY` and use one style per service.
- Use real Sokha Mart data in examples (`KHR`, `៛`, `PCS`, `KG`).

## 7. Git

- Branch first (`feat/…`, `fix/…`); never commit on `main`.
- Use Conventional Commits (`feat(currency): …`, `refactor(unit): …`, `docs: …`).
- One logical change per commit.
